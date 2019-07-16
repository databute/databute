package databute.databuter.cluster.handshake;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeMessageDeserializer implements MessageDeserializer<HandshakeMessage> {

    @Override
    public HandshakeMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        return new HandshakeMessage(id);
    }
}
