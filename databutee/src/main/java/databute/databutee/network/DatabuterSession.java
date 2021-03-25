package databute.databutee.network;

import com.google.common.base.MoreObjects;
import databute.databutee.Databutee;
import databute.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSession extends AbstractSession {

    private final Databutee databutee;

    public DatabuterSession(Databutee databutee, SocketChannel channel) {
        super(channel);
        this.databutee = checkNotNull(databutee, "databutee");
    }

    public Databutee databutee() {
        return databutee;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .toString();
    }
}
