package databute.databuter.entry.result.fail;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationFailMessageDeserializer implements MessageDeserializer<EntryOperationFailMessage> {

    @Override
    public EntryOperationFailMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntryOperationErrorCode errorCode = EntryOperationErrorCode.valueOf(packet.readString());
        return new EntryOperationFailMessage(id, key, errorCode);
    }
}
