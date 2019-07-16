package databute.databuter.cluster.network;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.ClusterNode;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterSession extends AbstractSession {

    private ClusterNode node;

    public ClusterSession(SocketChannel channel) {
        super(channel);
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
