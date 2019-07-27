package databute.databuter.cluster.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.AbstractClusterNode;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.ClusterNodeConfiguration;
import databute.databuter.cluster.network.ClusterSession;
import databute.databuter.cluster.network.ClusterSessionConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoteClusterNode extends AbstractClusterNode {

    private static final Logger logger = LoggerFactory.getLogger(RemoteClusterNode.class);

    private ClusterSession session;

    private final ClusterCoordinator clusterCoordinator;

    public RemoteClusterNode(ClusterNodeConfiguration configuration, ClusterCoordinator clusterCoordinator) {
        super(configuration);
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
    }

    public ClusterSession session() {
        return session;
    }

    public RemoteClusterNode session(ClusterSession session) {
        this.session = checkNotNull(session, "session");
        return this;
    }

    public void connect() {
        final String address = configuration().inboundEndpoint().address();
        final int port = configuration().inboundEndpoint().port();
        final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
        logger.info("Connecting to remote cluster node {} at {}", id(), remoteAddress);

        ClusterSessionConnector connector = new ClusterSessionConnector(clusterCoordinator.loopGroup(), clusterCoordinator, this);
        connector.connect(remoteAddress);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration())
                .add("session", session)
                .toString();
    }
}
