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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class BucketCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(BucketCoordinator.class);

    private PathChildrenCache cache;

    private final BucketGroup bucketGroup;
    private final ZooKeeperConfiguration zooKeeperConfiguration;
    private final AtomicLong availableBucketCount;
    private final SharedBucketFactor bucketFactor;

    public BucketCoordinator() {
        this.bucketGroup = Databuter.instance().bucketGroup();
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
        this.availableBucketCount = new AtomicLong();
        this.bucketFactor = new SharedBucketFactor();
    }

    public void start() throws BucketException {
        bucketFactor.start();

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
            createLocalActiveBucket();
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
                createLocalStandbyBucket(bucketConfiguration);
            } else {
                final RemoteBucket remoteBucket = createRemoteBucket(bucketConfiguration);
                logger.info("Added remote bucket {} because it is already high available.", remoteBucket.id());
            }
        } else {
            final RemoteBucket remoteBucket = createRemoteBucket(bucketConfiguration);
            logger.info("Added remote bucket {} because available bucket count is zero.", remoteBucket.id());
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

    private void createLocalActiveBucket() {
        final String nodeId = Databuter.instance().id();
        final BucketConfiguration bucketConfiguration = new BucketConfiguration().activeNodeId(nodeId);

        final LocalBucket localBucket = createLocalBucket(bucketConfiguration);
        logger.info("Created local active bucket {}.", localBucket.id());

        try {
            final String path = localBucket.save();
            logger.debug("Saved local active bucket {} to the ZooKeeper with path {}", localBucket.id(), path);

            logger.debug("BucketFactor : {} ", bucketFactor.getAndIncreaseBucketFactor());
        } catch (BucketException e) {
            logger.error("Failed to create local active bucket.", e);

            bucketGroup.remove(localBucket);
        }
    }

    private void createLocalStandbyBucket(BucketConfiguration bucketConfiguration) {
        final String nodeId = Databuter.instance().id();

        bucketConfiguration.standbyNodeId(nodeId);
        final LocalBucket localBucket = createLocalBucket(bucketConfiguration);
        logger.info("Created local standby bucket {}.", localBucket.id());

        try {
            localBucket.update();
            logger.info("Updated local standby bucket {} to the ZooKeeper.", localBucket.id());
        } catch (BucketException e) {
            logger.error("Failed to update local standby bucket.", e);

            // TODO(@ghkim3221): ZooKeeper에 업데이트가 실패한다면 어떻게 처리할 것인가?
            bucketGroup.remove(localBucket);
        }
    }

    private LocalBucket createLocalBucket(BucketConfiguration bucketConfiguration) {
        final LocalBucket localBucket = new LocalBucket(bucketConfiguration);

        bucketGroup.add(localBucket);
        availableBucketCount.decrementAndGet();

        return localBucket;
    }

    private RemoteBucket createRemoteBucket(BucketConfiguration bucketConfiguration) {
        final RemoteBucket remoteBucket = new RemoteBucket(bucketConfiguration);

        bucketGroup.add(remoteBucket);

        return remoteBucket;
    }
}
