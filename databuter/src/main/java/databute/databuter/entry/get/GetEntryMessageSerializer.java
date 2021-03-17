package databute.databuter.entry.get;

import databute.network.message.MessageSerializer;
import databute.network.packet.BufferedPacket;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntryMessageSerializer implements MessageSerializer<GetEntryMessage> {

    @Override
    public Packet serialize(GetEntryMessage getEntryMessage) {
        checkNotNull(getEntryMessage, "getEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(getEntryMessage.id());
        packet.writeString(getEntryMessage.key());
        return packet;
    }
}
