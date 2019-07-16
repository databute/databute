package databute.databuter.cluster.handshake;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeRequestMessageSerializer implements MessageSerializer<HandshakeRequestMessage> {

    @Override
    public Packet serialize(HandshakeRequestMessage handshakeRequest) {
        checkNotNull(handshakeRequest, "handshakeRequest");

        final Packet packet = new BufferedPacket();
        packet.writeString(handshakeRequest.id());
        return packet;
    }
}
