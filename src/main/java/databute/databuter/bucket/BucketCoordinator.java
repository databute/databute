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

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(BucketCoordinator.class);

    private PathChildrenCache cache;

    private final BucketGroup bucketGroup;
    private final AtomicLong availableBucketCount;
    private final ZooKeeperConfiguration zooKeeperConfiguration;

    public BucketCoordinator(BucketGroup bucketGroup) {
        this.bucketGroup = bucketGroup;
        this.availableBucketCount = new AtomicLong();
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
    }

    public void start() throws BucketException {
        calculateAvailableBucketCount();

        registerCacheEvenetListener();
    }

    private void calculateAvailableBucketCount() {
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long availableMemory = totalMemory - Databuter.instance().configuration().guardMemorySizeMb();

        this.availableBucketCount.set(availableMemory / Databuter.instance().configuration().bucketMemorySizeMb());
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
        addBackUpBucketIfNeeded();

        try {
            makeBucket();
        } catch (BucketException e) {
            logger.error("Failed making Bucket.", e);
        }
    }

    private void onAdded(ChildData data) {
        checkNotNull(data, "data");

        final String json = new String(data.getData());
        final BucketConfiguration addedBucketConfiguration = new Gson().fromJson(json, BucketConfiguration.class);

        if (bucketGroup.has(addedBucketConfiguration.id())) {
            //로컬 버킷
            return;
        }

        bucketGroup.add(new RemoteBucket(addedBucketConfiguration));
    }

    //TODO(@nono5546):업데이트 구현 후 테스트.
    private void onUpdated(ChildData data) {
        checkNotNull(data, "data");

        final String json = new String(data.getData());
        final BucketConfiguration updatedBucketConfiguration = new Gson().fromJson(json, BucketConfiguration.class);

        final Bucket bucket = bucketGroup.find(updatedBucketConfiguration.id());
        if (bucket == null) {
            //TODO(@nono5546):에러 발생할 수 있음.
            bucketGroup.add(new RemoteBucket(updatedBucketConfiguration));
        } else {
            bucket.updateConfiguration(updatedBucketConfiguration);
        }
    }

    private void addBackUpBucketIfNeeded() {
        for (Bucket bucket : bucketGroup) {
            if (availableBucketCount.get() <= 0) {
                break;
            }

            if (StringUtils.isEmpty(bucket.backUpClusterId())) {
                try {
                    final String nodeId = Databuter.instance().id();
                    bucket.configuration().backupClusterId(nodeId);

                    bucketGroup.add(bucket);

                    updateBackupBucket(bucket);

                    availableBucketCount.getAndDecrement();
                } catch (Exception e) {
                    logger.error("Failed to register backup bucket {}", bucket.id(), e);
                }
            }
        }
    }

    private void updateBackupBucket(Bucket bucket) throws Exception {
        final String json = new Gson().toJson(bucket.configuration());
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket", bucket.id());

        Databuter.instance().curator()
                .setData()
                .forPath(path, json.getBytes());
        logger.debug("Bucket {} is updated.", bucket.id());
    }

    private void makeBucket() throws BucketException {
        logger.debug("Making {} bucket...", availableBucketCount.get());

        while (availableBucketCount.get() > 0) {
            final String nodeId = Databuter.instance().id();
            final BucketConfiguration bucketConfiguration = new BucketConfiguration().masterClusterId(nodeId);
            final Bucket bucket = new LocalBucket(bucketConfiguration);
            final boolean added = bucketGroup.add(bucket);
            if (!added) {
                throw new BucketException("Found duplcated bucket " + bucket);
            }

            try {
                registerBucket(bucket);
            } catch (Exception e) {
                throw new BucketException("Failed to register master bucket " + bucket, e);
            }

            availableBucketCount.decrementAndGet();
        }
    }

    private void registerBucket(Bucket bucket) throws Exception {
        final String json = new Gson().toJson(bucket.configuration());
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "bucket", bucket.id());
        final String createdPath = Databuter.instance().curator()
                .create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, json.getBytes());
        logger.debug("Registered bucket {} at {}", bucket.id(), createdPath);
    }
}
