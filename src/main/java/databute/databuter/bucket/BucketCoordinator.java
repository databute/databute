package databute.databuter.bucket;

import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.ZooKeeperConfiguration;
import databute.databuter.bucket.local.LocalBucket;
import databute.databuter.bucket.remote.RemoteBucket;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class BucketCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(BucketCoordinator.class);

    private PathChildrenCache cache;

    private final BucketGroup bucketGroup;
    private final ZooKeeperConfiguration zooKeeperConfiguration;
    private final AtomicLong availableBucketCount;

    public BucketCoordinator() {
        this.bucketGroup = Databuter.instance().bucketGroup();
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
        this.availableBucketCount = new AtomicLong();
    }

    public void start() throws BucketException {
        calculateAvailableBucketCount();

        registerCacheEvenetListener();
    }

    private void calculateAvailableBucketCount() {
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long availableMemory = (totalMemory - Databuter.instance().configuration().guardMemorySize());
        final long availableBucketCount = (availableMemory / Databuter.instance().configuration().bucketMemorySize());
        this.availableBucketCount.set(availableBucketCount);
    }

    private void registerCacheEvenetListener() throws BucketException {
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket");

        try {
            cache = new PathChildrenCache(Databuter.instance().curator(), path, true);
            cache.getListenable().addListener(this::onCacheEvent);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            throw new BucketException("Failed to register cache event listener.", e);
        }
    }

    @SuppressWarnings("unused")
    private void onCacheEvent(CuratorFramework curator, PathChildrenCacheEvent event) {
        switch (event.getType()) {
            case INITIALIZED:
                onInitialized();
                break;
            case CHILD_ADDED:
                onAdded(event.getData());
                break;
            case CHILD_UPDATED:
                onUpdated(event.getData());
                break;
        }
    }

    private void onInitialized() {
        while (availableBucketCount.get() > 0) {
            try {
                final String nodeId = Databuter.instance().id();
                final BucketConfiguration bucketConfiguration = new BucketConfiguration().activeNodeId(nodeId);
                final Bucket localBucket = new LocalBucket(bucketConfiguration);

                final String json = new Gson().toJson(localBucket.configuration());
                final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket", localBucket.id());
                final String createdPath = Databuter.instance().curator()
                        .create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path, json.getBytes());

                final boolean added = bucketGroup.add(localBucket);
                if (added) {
                    final long remainingBucketCount = availableBucketCount.decrementAndGet();
                    logger.info("Added local active bucket {}. {} buckets are remaining.", localBucket.id(), remainingBucketCount);
                } else {
                    logger.error("Failed to add local active bucket {}", localBucket);
                }
            } catch (Exception e) {
                logger.error("Failed to register local active bucket to the ZooKeeper.", e);
            }
        }
    }

    private void onAdded(ChildData data) {
        if (data == null) {
            logger.warn("Received CHILD_ADDED event with null data.");
            return;
        }

        final String json = new String(data.getData());
        final BucketConfiguration bucketConfiguration = new Gson().fromJson(json, BucketConfiguration.class);

        if (bucketGroup.has(bucketConfiguration.id())) {
            // 이미 버킷 그룹에 버킷이 추가되어있는 경우 무시한다.
            return;
        }

        final String nodeId = Databuter.instance().id();
        if (bucketConfiguration.isActiveBy(nodeId) || bucketConfiguration.isStandbyBy(nodeId)) {
            // 이 노드의 버킷임에도 불구하고 버킷 그룹에 추가되어있지 않는 경우
            throw new IllegalStateException("Found not added own bucket " + bucketConfiguration);
        }

        if (availableBucketCount.get() > 0) {
            if (StringUtils.isEmpty(bucketConfiguration.standbyNodeId())) {
                try {
                    bucketConfiguration.standbyNodeId(nodeId);
                    final Bucket localBucket = new LocalBucket(bucketConfiguration);

                    final String json2 = new Gson().toJson(localBucket.configuration());
                    final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket", localBucket.id());
                    Databuter.instance().curator()
                            .setData()
                            .forPath(path, json2.getBytes());

                    final boolean added = bucketGroup.add(localBucket);
                    if (added) {
                        final long remainingBucketCount = availableBucketCount.decrementAndGet();
                        logger.info("Added local standby bucket {}. {} buckets are remaining.",
                                localBucket.id(), remainingBucketCount);
                    } else {
                        logger.error("Failed to add local standby bucket {}", localBucket);
                    }
                } catch (Exception e) {
                    logger.error("Failed to register local standby bucket to the ZooKeeper.", e);
                }
            } else {
                final Bucket remoteBucket = new RemoteBucket(bucketConfiguration);
                final boolean added = bucketGroup.add(remoteBucket);
                if (added) {
                    logger.info("Added remote bucket {} because it is already high available.", remoteBucket.id());
                } else {
                    logger.error("Failed to add remote bucket {}", remoteBucket);
                }
            }
        } else {
            final Bucket remoteBucket = new RemoteBucket(bucketConfiguration);
            final boolean added = bucketGroup.add(remoteBucket);
            if (added) {
                logger.info("Added remote bucket {} because available bucket count is zero.", remoteBucket.id());
            } else {
                logger.error("Failed to add remote bucket {}", remoteBucket);
            }
        }
    }

    private void onUpdated(ChildData data) {
        if (data == null) {
            logger.warn("Received CHILD_UPDATED event with null data.");
            return;
        }

        final String json = new String(data.getData());
        final BucketConfiguration bucketConfiguration = new Gson().fromJson(json, BucketConfiguration.class);

        final Bucket bucket = bucketGroup.find(bucketConfiguration.id());
        if (bucket == null) {
            throw new IllegalStateException("Found not added bucket " + bucketConfiguration);
        }

        bucket.configuration().update(bucketConfiguration);
        logger.info("Updated bucket {}.", bucket.id());
    }
}
