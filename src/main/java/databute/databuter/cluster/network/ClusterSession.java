package databute.databuter.cluster.network;

import com.google.common.base.MoreObjects;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

public class ClusterSession extends AbstractSession {

    public ClusterSession(SocketChannel channel) {
        super(channel);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .toString();
    }
}
