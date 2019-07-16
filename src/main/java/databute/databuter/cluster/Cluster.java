package databute.databuter.cluster;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import databute.databuter.cluster.coordinator.ClusterCoordinator;
import databute.databuter.cluster.network.ClusterSessionAcceptor;
import databute.databuter.cluster.network.ClusterSessionConnector;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Cluster {

    private static final Logger logger = LoggerFactory.getLogger(Cluster.class);

    private ClusterSessionAcceptor acceptor;
    private ClusterCoordinator coordinator;

    private final ClusterConfiguration configuration;

    private final String id;
    private final EventLoopGroup loopGroup;
    private final Map<String, ClusterNode> nodes;

    public Cluster(ClusterConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.id = UUID.randomUUID().toString();
        this.loopGroup = new NioEventLoopGroup();
        this.nodes = Maps.newConcurrentMap();
    }

    public String id() {
        return id;
    }

    public void registerNode(ClusterNode node) {
        checkNotNull(node, "node");

        if (isRegisteredNode(node)) {
            throw new IllegalStateException("Already registered node " + node.id());
        }

        nodes.put(node.id(), node);
        if (logger.isDebugEnabled()) {
            logger.debug("Registered node {}", node);
        } else {
            logger.info("Registered node {}", node.id());
        }

        connectToNode(node);
    }

    private void connectToNode(ClusterNode node) {
        checkNotNull(node, "node");

        final InetSocketAddress remoteAddress = new InetSocketAddress(node.address(), node.port());
        logger.info("Connecting to node {} at {}", node.id(), remoteAddress);

        final ClusterSessionConnector connector = new ClusterSessionConnector(loopGroup, this, node);
        connector.connect(remoteAddress);
    }

    public boolean isRegisteredNode(ClusterNode node) {
        return isRegisteredNode(node.id());
    }

    public boolean isRegisteredNode(String id) {
        return nodes.containsKey(id);
    }

    public void join() throws ClusterException {
        bindAcceptor();
        connectToCoordinator();
    }

    private void bindAcceptor() {
        final InetSocketAddress localAddress = configuration.localAddress();
        acceptor = new ClusterSessionAcceptor(loopGroup, this);
        acceptor.bind(localAddress).join();
        logger.debug("Cluster session acceptor is bound on {}", acceptor.localAddress());
    }

    private void connectToCoordinator() throws ClusterException {
        coordinator = new ClusterCoordinator(this, configuration.coordinator());
        coordinator.connect();
    }

    public ClusterNode toClusterNode() {
        return new ClusterNode(id, configuration.address(), configuration.port());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
