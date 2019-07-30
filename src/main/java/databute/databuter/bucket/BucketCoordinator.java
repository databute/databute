package databute.databuter.bucket;

import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.ZooKeeperConfiguration;
import databute.databuter.bucket.local.LocalBucket;
import databute.databuter.bucket.notification.BucketNotificationMessage;
import databute.databuter.bucket.remote.RemoteBucket;
import databute.databuter.client.network.ClientSessionGroup;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.ClusterNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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
    private final SharedKeyFactorCounter sharedKeyFactorCounter;
    private final InterProcessMutex bucketMutex;

    public BucketCoordinator() {
        this.bucketGroup = Databuter.instance().bucketGroup();
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
        this.availableBucketCount = new AtomicLong();
        this.sharedKeyFactorCounter = new SharedKeyFactorCounter();

        final CuratorFramework curator = Databuter.instance().curator();
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "mutex/bucket");
        this.bucketMutex = new InterProcessMutex(curator, path);
    }

    public void start() throws SharedKeyFactorException, BucketException {
        sharedKeyFactorCounter.start();

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
            case CHILD_REMOVED:
                onRemoved(event.getData());
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

                syncActiveClusterNode(remoteBucket);
                syncStandbyClusterNode(remoteBucket);
            }
        } else {
            final RemoteBucket remoteBucket = createRemoteBucket(bucketConfiguration);
            logger.info("Added remote bucket {} because available bucket count is zero.", remoteBucket.id());

            syncActiveClusterNode(remoteBucket);
            syncStandbyClusterNode(remoteBucket);
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

        final boolean updated = bucket.configuration().update(bucketConfiguration);

        if (updated) {
            logger.info("Updated bucket {}.", bucket.id());

            syncActiveClusterNode(bucket);
            syncStandbyClusterNode(bucket);

            broadcastBucketUpdated(bucket);
        }
    }

    private void onRemoved(ChildData data) {
        if (data == null) {
            logger.warn("Received CHILD_REMOVED event with null data.");
            return;
        }

        final String json = new String(data.getData());
        final BucketConfiguration bucketConfiguration = new Gson().fromJson(json, BucketConfiguration.class);

        Bucket bucket = bucketGroup.find(bucketConfiguration.id());
        if (bucket instanceof LocalBucket) {
            throw new IllegalStateException("Remove local bucket " + bucketConfiguration);
        } else {
            bucketGroup.remove(bucket);
        }
    }

    private void broadcastBucketUpdated(Bucket bucket) {
        final ClientSessionGroup clientSessionGroup = Databuter.instance().clientSessionGroup();
        clientSessionGroup.broadcastToListeningSession(BucketNotificationMessage.updated()
                .id(bucket.id())
                .keyFactor(bucket.keyFactor())
                .activeNodeId(bucket.activeNodeId())
                .standbyNodeId(bucket.standbyNodeId())
                .build());
    }

    private void createLocalActiveBucket() {
        final String nodeId = Databuter.instance().id();
        final BucketConfiguration bucketConfiguration = new BucketConfiguration().activeNodeId(nodeId);

        LocalBucket localBucket = null;
        try {
            bucketMutex.acquire();

            try {
                localBucket = createLocalBucket(bucketConfiguration);
                logger.info("Created local active bucket {}.", localBucket.id());

                syncActiveClusterNode(localBucket);
                syncStandbyClusterNode(localBucket);

                final int keyFactor = sharedKeyFactorCounter.getAndIncreaseSharedKeyFactor();
                localBucket.configuration().keyFactor(keyFactor);
                logger.info("Assigned key factor {} to bucket {}", localBucket.configuration().keyFactor(), localBucket.id());

                final String path = save(localBucket);
                logger.debug("Saved local active bucket {} to the ZooKeeper with path {}", localBucket.id(), path);
            } catch (SharedKeyFactorException e) {
                logger.error("Failed to increase shared key factor.", e);

                bucketGroup.remove(localBucket);
            } catch (BucketException e) {
                logger.error("Failed to create local active bucket.", e);

                bucketGroup.remove(localBucket);
                try {
                    sharedKeyFactorCounter.getAndDecreaseSharedKeyFactor();
                } catch (SharedKeyFactorException e1) {
                    logger.error("Failed to decrease shared key factor.", e1);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to acquire bucket mutex.");
        } finally {
            try {
                bucketMutex.release();
            } catch (Exception e) {
                logger.error("Failed to realese bucket mutex.");
            }
        }
    }

    private void createLocalStandbyBucket(BucketConfiguration bucketConfiguration) {
        final String nodeId = Databuter.instance().id();

        bucketConfiguration.standbyNodeId(nodeId);
        final LocalBucket localBucket = createLocalBucket(bucketConfiguration);
        logger.info("Created local standby bucket {}.", localBucket.id());
        //TODO(@nono5546): bucketGroup에 localBucket 추가.

        syncActiveClusterNode(localBucket);
        syncStandbyClusterNode(localBucket);

        try {
            update(localBucket);
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

    private String save(Bucket bucket) throws BucketException {
        // TODO(@ghkim3221): 비동기로 ZooKeeper에 저장
        try {
            final String json = new Gson().toJson(bucket.configuration());
            final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
            final String path = ZKPaths.makePath(zooKeeperPath, "bucket", bucket.id());

            bucketMutex.acquire();

            try {
                return Databuter.instance().curator()
                        .create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path, json.getBytes());
            } catch (Exception e) {
                throw new BucketException("Failed to save bucket " + bucket.id() + " to the ZooKeeper.");
            }
        } catch (BucketException e) {
            throw e;
        } catch (Exception e) {
            throw new BucketException("Failed to acquire bucket mutex.");
        } finally {
            try {
                bucketMutex.release();
            } catch (Exception e) {
                logger.error("Failed to release bucket mutex.", e);
            }
        }
    }

    public void update(Bucket bucket) throws BucketException {
        // TODO(@ghkim3221): 비동기로 ZooKeeper에 업데이트
        try {
            final String json = new Gson().toJson(bucket.configuration());
            final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
            final String path = ZKPaths.makePath(zooKeeperPath, "bucket", bucket.id());

            bucketMutex.acquire();
            try {
                Databuter.instance().curator()
                        .setData()
                        .forPath(path, json.getBytes());
            } catch (Exception e) {
                throw new BucketException("Failed to update bucket " + bucket.id() + " to the ZooKeeper.");
            }
        } catch (BucketException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to acquire bucket mutex.", e);
        } finally {
            try {
                bucketMutex.release();
            } catch (Exception e) {
                logger.error("Failed to release bucket mutex.", e);
            }
        }
    }

    private void syncActiveClusterNode(Bucket bucket) {
        if (StringUtils.isEmpty(bucket.configuration().activeNodeId())) {
            return;
        }

        final ClusterCoordinator clusterCoordinator = Databuter.instance().clusterCoordinator();
        if (clusterCoordinator == null) {
            return;
        }
        final ClusterNode clusterNode = clusterCoordinator.find(bucket.activeNodeId());
        if (clusterNode != null) {
            // 현재 Cluster 노드와 연결되어있지 않더라도, Cluster 노드가 연결될 때 버킷을 찾아 연결 할 것
            bucket.activeNode(clusterNode);
            logger.debug("Synchronized active cluster node {} to bucket {}", clusterNode.id(), bucket.id());
        }
    }

    private void syncStandbyClusterNode(Bucket bucket) {
        if (StringUtils.isEmpty(bucket.configuration().standbyNodeId())) {
            return;
        }

        final ClusterCoordinator clusterCoordinator = Databuter.instance().clusterCoordinator();
        if (clusterCoordinator == null) {
            return;
        }
        final ClusterNode clusterNode = clusterCoordinator.find(bucket.standbyNodeId());
        if (clusterNode != null) {
            // 현재 Cluster 노드와 연결되어있지 않더라도, Cluster 노드가 연결될 때 버킷을 찾아 연결 할 것
            bucket.standbyNode(clusterNode);
            logger.debug("Synchronized standby cluster node {} to bucket {}", clusterNode.id(), bucket.id());
        }
    }
}
