package databute.databuter.cluster;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.BucketGroup;
import databute.databuter.cluster.coordinator.ClusterCoordinator;
import databute.databuter.cluster.coordinator.RemoteClusterNodeGroup;
import databute.databuter.cluster.network.ClusterSessionAcceptor;
import databute.databuter.cluster.node.ClusterNodeConfiguration;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Cluster {

    private static final Logger logger = LoggerFactory.getLogger(Cluster.class);

    private ClusterSessionAcceptor acceptor;
    private ClusterCoordinator coordinator;

    private final ClusterConfiguration configuration;

    private final String id;
    private final EventLoopGroup loopGroup;
    private final LocalClusterNode localNode;
    private final RemoteClusterNodeGroup remoteNodeGroup;
    //TODO(@nono5546):coordinator 리팩토링 때 삭제
    private final BucketGroup bucketGroup;

    public Cluster(ClusterConfiguration configuration, BucketGroup bucketGroup) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.id = UUID.randomUUID().toString();
        this.loopGroup = new NioEventLoopGroup();
        this.localNode = new LocalClusterNode(ClusterNodeConfiguration.builder()
                .id(id)
                .address(configuration.address())
                .port(configuration.port())
                .build());
        this.remoteNodeGroup = new RemoteClusterNodeGroup();
        //TODO(@nono5546):coordinator 리팩토링 때 삭제
        this.bucketGroup = bucketGroup;
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
        coordinator = new ClusterCoordinator(this, configuration.coordinator(), bucketGroup);
        coordinator.connect();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
