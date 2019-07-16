package databute.databuter.cluster.handshake;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeMessageSerializer implements MessageSerializer<HandshakeMessage> {

    @Override
    public Packet serialize(HandshakeMessage handshake) {
        checkNotNull(handshake, "handshake");

        final Packet packet = new BufferedPacket();
        packet.writeString(handshake.id());
        return packet;
    }
}
