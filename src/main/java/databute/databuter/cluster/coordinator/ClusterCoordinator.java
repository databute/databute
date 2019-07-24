package databute.databuter.cluster.coordinator;

import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketGroup;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.ClusterException;
import databute.databuter.cluster.node.ClusterNodeConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterCoordinator.class);

    private PathChildrenCache cache;

    private final Cluster cluster;
    private final BucketGroup bucketGroup;

    public ClusterCoordinator(Cluster cluster, BucketGroup bucketGroup) {
        this.cluster = checkNotNull(cluster, "cluster");
        this.bucketGroup = bucketGroup;
    }

    public void connect() throws ClusterException {
        try {
            registerCacheEventListener();
        } catch (Exception e) {
            throw new ClusterException("Failed to register cache event listener.", e);
        }
    }

    private void registerCacheEventListener() throws Exception {
        final String path = ZKPaths.makePath(cluster.configuration().path(), "discovery");

        cache = new PathChildrenCache(Databuter.instance().curator(), path, true);
        cache.getListenable().addListener(this::onCacheEvent);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        logger.debug("Registered cache event listener at {}", path);
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
        }
    }

    private void onInitialized() {
        try {
            registerClusterNode();
        } catch (Exception e) {
            logger.error("Failed to register cluster node.", e);
        }

        try {
            registerBucketGroup();
        } catch (Exception e) {
            logger.error("Failed to register bucket group.", e);
        }
    }

    private void onAdded(ChildData data) {
        checkNotNull(data, "data");

        final String json = new String(data.getData());
        final ClusterNodeConfiguration nodeConfiguration = new Gson().fromJson(json, ClusterNodeConfiguration.class);
        final RemoteClusterNode remoteNode = new RemoteClusterNode(nodeConfiguration, cluster);

        if (StringUtils.equals(cluster.id(), remoteNode.id())) {
            // 로컬 노드
            return;
        }

        final boolean added = cluster.remoteNodeGroup().add(remoteNode);
        if (added) {
            remoteNode.connect();
        }
    }

    private void registerClusterNode() throws Exception {
        final String json = new Gson().toJson(cluster.localNode().configuration());
        final String path = ZKPaths.makePath(cluster.configuration().path(), "discovery", cluster.id());
        final String createdPath = Databuter.instance().curator()
                .create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, json.getBytes());
        logger.debug("Registered cluster node at {}", createdPath);
    }

    private void registerBucketGroup() throws Exception {
        for (Bucket bucket : bucketGroup) {
            final String json = new Gson().toJson(bucket);
            final String path = ZKPaths.makePath(cluster.configuration().path(), "bucket", bucket.id());
            final String createdPath = Databuter.instance().curator()
                    .create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, json.getBytes());
            logger.debug("Registered Bucket node at {}", createdPath);
        }
    }
}
