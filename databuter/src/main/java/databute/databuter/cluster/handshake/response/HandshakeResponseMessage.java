package databute.databuter.cluster.handshake.response;

import com.google.common.base.MoreObjects;
import databute.network.message.Message;
import databute.network.message.MessageCode;

public class HandshakeResponseMessage implements Message {

    @Override
    public MessageCode messageCode() {
        return MessageCode.HANDSHAKE_RESPONSE;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
