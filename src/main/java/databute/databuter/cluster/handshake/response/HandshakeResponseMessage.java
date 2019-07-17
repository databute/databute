package databute.databuter.cluster.handshake.response;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.network.ClusterMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

public class HandshakeResponseMessage implements Message {

    @Override
    public MessageCode messageCode() {
        return ClusterMessageCode.HANDSHAKE;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
