package databute.databuter.cluster.handshake.response;

import databute.network.message.MessageSerializer;
import databute.network.packet.BufferedPacket;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandshakeResponseMessageSerializer implements MessageSerializer<HandshakeResponseMessage> {

    @Override
    public Packet serialize(HandshakeResponseMessage handshakeResponse) {
        checkNotNull(handshakeResponse, handshakeResponse);

        return new BufferedPacket();
    }
}
