package databute.databuter.cluster.handshake;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.network.ClusterMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeMessage implements Message {

    private final String id;

    public HandshakeMessage(String id) {
        this.id = checkNotNull(id, "id");
    }

    @Override
    public MessageCode messageCode() {
        return ClusterMessageCode.HANDSHAKE;
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
