package databute.databuter.cluster.handshake.response;

import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeResponseMessageDeserializer implements MessageDeserializer<HandshakeResponseMessage> {

    @Override
    public HandshakeResponseMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        return new HandshakeResponseMessage();
    }
}
