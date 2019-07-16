package databute.databuter.cluster.network;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.ClusterNode;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterSession extends AbstractSession {

    private ClusterNode node;

    private final Cluster cluster;

    public ClusterSession(SocketChannel channel, Cluster cluster) {
        super(channel);
        this.cluster = checkNotNull(cluster, "cluster");
    }

    public Cluster cluster() {
        return cluster;
    }

    public ClusterNode node() {
        return node;
    }

    public ClusterSession node(ClusterNode node) {
        this.node = checkNotNull(node, "node");
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .add("node", node)
                .toString();
    }
}
