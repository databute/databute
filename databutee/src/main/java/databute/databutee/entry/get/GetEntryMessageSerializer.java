package databute.databutee.entry.get;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

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
