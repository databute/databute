package databute.databuter.cluster.coordinator;

import com.google.gson.Gson;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.ClusterException;
import databute.databuter.cluster.ClusterNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterCoordinator.class);

    private PathChildrenCache cache;

    private final Cluster cluster;
    private final ClusterCoordinatorConfiguration configuration;

    private final CuratorFramework curator;

    public ClusterCoordinator(Cluster cluster, ClusterCoordinatorConfiguration configuration) {
        this.cluster = checkNotNull(cluster, "cluster");
        this.configuration = checkNotNull(configuration, "configuration");
        this.curator = CuratorFrameworkFactory.builder()
                .connectString(configuration.connectString())
                .retryPolicy(new ExponentialBackoffRetry(configuration.baseSleepTimeMs(), configuration.maxRetries()))
                .build();
    }

    public void connect() throws ClusterException {
        try {
            connectToZooKeeper();
        } catch (InterruptedException e) {
            throw new ClusterException("Failed to connect to the ZooKeeper.", e);
        }
        try {
            registerCacheEventListener();
        } catch (Exception e) {
            throw new ClusterException("Failed to register cache event listener.", e);
        }
    }

    private void connectToZooKeeper() throws InterruptedException {
        logger.debug("Connecting to the ZooKeeper...");
        curator.start();
        curator.blockUntilConnected();
        logger.debug("Connected with the ZooKeeper.");
    }

    private void registerCacheEventListener() throws Exception {
        final String path = ZKPaths.makePath(configuration.path(), "discovery");

        cache = new PathChildrenCache(curator, path, true);
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
    }

    private void onAdded(ChildData data) {
        checkNotNull(data, "data");

        final String json = new String(data.getData());
        final ClusterNode node = new Gson().fromJson(json, ClusterNode.class);

        if (StringUtils.equals(cluster.id(), node.id())) {
            // 로컬 노드
            return;
        }

        final boolean added = cluster.nodeGroup().add(node);
        if (added) {
            cluster.connectToNode(node);
        }
    }

    private void registerClusterNode() throws Exception {
        final String json = new Gson().toJson(cluster.toClusterNode());
        final String path = curator.create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(ZKPaths.makePath(configuration.path(), "discovery", cluster.id()), json.getBytes());
        logger.debug("Registered cluster node at {}", path);
    }
}
