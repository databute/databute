package databute.databuter.client.network;

import com.google.common.base.MoreObjects;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

public class ClientSession extends AbstractSession {

    public ClientSession(SocketChannel channel) {
        super(channel);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .toString();
    }
}
