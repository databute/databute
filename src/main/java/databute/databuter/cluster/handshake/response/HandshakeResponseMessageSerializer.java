package databute.databuter.cluster.handshake.response;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeResponseMessageSerializer implements MessageSerializer<HandshakeResponseMessage> {

    @Override
    public Packet serialize(HandshakeResponseMessage handshakeResponse) {
        checkNotNull(handshakeResponse, handshakeResponse);

        final Packet packet = new BufferedPacket();
        return packet;
    }
}
