package databute.databuter.client.network;

import com.google.common.base.MoreObjects;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

public class ClientSession extends AbstractSession {

    private boolean listening;

    public ClientSession(SocketChannel channel) {
        super(channel);
    }

    public boolean isListening() {
        return listening;
    }

    public void startListen() {
        this.listening = true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .add("listening", listening)
                .toString();
    }
}
