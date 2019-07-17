package databute.databuter.cluster.handshake.request;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeRequestMessageDeserializer implements MessageDeserializer<HandshakeRequestMessage> {

    @Override
    public HandshakeRequestMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        return new HandshakeRequestMessage(id);
    }
}
