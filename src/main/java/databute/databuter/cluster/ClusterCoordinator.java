package databute.databuter.cluster;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.ZooKeeperConfiguration;
import databute.databuter.cluster.local.LocalClusterNode;
import databute.databuter.cluster.network.ClusterSessionAcceptor;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.cluster.remote.RemoteClusterNodeGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterCoordinator.class);

    private ClusterSessionAcceptor acceptor;
    private PathChildrenCache cache;

    private final ClusterConfiguration configuration;

    private final String id;
    private final EventLoopGroup loopGroup;
    private final LocalClusterNode localNode;
    private final RemoteClusterNodeGroup remoteNodeGroup;
    private final ZooKeeperConfiguration zooKeeperConfiguration;

    public ClusterCoordinator(ClusterConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.id = UUID.randomUUID().toString();
        this.loopGroup = new NioEventLoopGroup();
        this.localNode = new LocalClusterNode(ClusterNodeConfiguration.builder()
                .id(id)
                .address(configuration.address())
                .port(configuration.port())
                .build());
        this.remoteNodeGroup = new RemoteClusterNodeGroup();
        this.zooKeeperConfiguration = Databuter.instance().configuration().zooKeeper();
    }

    public String id() {
        return id;
    }

    public EventLoopGroup loopGroup() {
        return loopGroup;
    }

    public LocalClusterNode localNode() {
        return localNode;
    }

    public RemoteClusterNodeGroup remoteNodeGroup() {
        return remoteNodeGroup;
    }

    public void start() throws ClusterException {
        bindAcceptor();
        registerCacheEventListener();
    }

    private void bindAcceptor() {
        final InetSocketAddress localAddress = configuration.localAddress();
        acceptor = new ClusterSessionAcceptor(loopGroup, this);
        acceptor.bind(localAddress).join();
        logger.debug("Cluster session acceptor is bound on {}", acceptor.localAddress());
    }

    private void registerCacheEventListener() throws ClusterException {
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "discovery");

        try {
            cache = new PathChildrenCache(Databuter.instance().curator(), path, true);
            cache.getListenable().addListener(this::onCacheEvent);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            logger.debug("Registered cluster remote's cache event listener at {}", path);
        } catch (Exception e) {
            throw new ClusterException("Failed to register cache event listener.", e);
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
        }
    }

    private void onInitialized() {
        try {
            registerClusterNode();
        } catch (Exception e) {
            logger.error("Failed to register cluster node.", e);
            // TODO(@ghkim3221): 시스템 종료?
        }
    }

    private void onAdded(ChildData data) {
        checkNotNull(data, "data");

        final String json = new String(data.getData());
        final ClusterNodeConfiguration nodeConfiguration = new Gson().fromJson(json, ClusterNodeConfiguration.class);
        final RemoteClusterNode remoteNode = new RemoteClusterNode(nodeConfiguration, this);

        if (StringUtils.equals(id, remoteNode.id())) {
            // 로컬 노드
            return;
        }

        final boolean added = remoteNodeGroup.add(remoteNode);
        if (added) {
            remoteNode.connect();
        }
    }

    private void registerClusterNode() throws Exception {
        final String json = new Gson().toJson(localNode.configuration());
        final String path = ZKPaths.makePath(zooKeeperConfiguration.path(), "discovery", id);
        final String createdPath = Databuter.instance().curator()
                .create()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, json.getBytes());
        logger.debug("Registered cluster node at {}", createdPath);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
