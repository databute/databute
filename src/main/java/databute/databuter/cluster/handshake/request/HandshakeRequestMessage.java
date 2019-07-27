package databute.databuter.cluster.handshake.request;

import com.google.common.base.MoreObjects;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeRequestMessage implements Message {

    private final String id;

    public HandshakeRequestMessage(String id) {
        this.id = checkNotNull(id, "id");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.HANDSHAKE_REQUEST;
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
