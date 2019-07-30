package databute.databuter.entity.get;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessageSerializer implements MessageSerializer<GetEntityMessage> {

    @Override
    public Packet serialize(GetEntityMessage getEntityMessage) {
        checkNotNull(getEntityMessage, "getEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(getEntityMessage.id());
        packet.writeString(getEntityMessage.key());
        return packet;
    }
}
