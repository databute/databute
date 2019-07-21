package databute.databuter.cluster.coordinator;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.network.ClusterSessionConnector;
import databute.databuter.cluster.node.AbstractClusterNode;
import databute.databuter.cluster.node.ClusterNodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoteClusterNode extends AbstractClusterNode {

    private static final Logger logger = LoggerFactory.getLogger(RemoteClusterNode.class);

    private final Cluster cluster;

    public RemoteClusterNode(ClusterNodeConfiguration configuration, Cluster cluster) {
        super(configuration);
        this.cluster = checkNotNull(cluster, "cluster");
    }

    public void connect() {
        final String address = configuration().address();
        final int port = configuration().port();
        final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
        logger.info("Connecting to remote cluster node {} at {}", id(), remoteAddress);

        ClusterSessionConnector connector = new ClusterSessionConnector(cluster.loopGroup(), cluster, this);
        connector.connect(remoteAddress);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration())
                .toString();
    }
}
