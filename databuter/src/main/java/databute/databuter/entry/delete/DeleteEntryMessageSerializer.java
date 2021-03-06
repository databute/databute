package databute.databuter.entry.delete;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

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
