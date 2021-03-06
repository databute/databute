package databute.databutee.entry.delete;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntryMessageSerializer implements MessageSerializer<DeleteEntryMessage> {

    @Override
    public Packet serialize(DeleteEntryMessage deleteEntryMessage) {
        checkNotNull(deleteEntryMessage, "deleteEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(deleteEntryMessage.id());
        packet.writeString(deleteEntryMessage.key());
        return packet;
    }
}
