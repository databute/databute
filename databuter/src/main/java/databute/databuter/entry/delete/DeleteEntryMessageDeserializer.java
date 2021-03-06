package databute.databuter.entry.delete;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntryMessageDeserializer implements MessageDeserializer<DeleteEntryMessage> {

    @Override
    public DeleteEntryMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        return new DeleteEntryMessage(id, key);
    }
}
