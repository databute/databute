package databute.databuter.entity.delete;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntityMessageDeserializer implements MessageDeserializer<DeleteEntityMessage> {

    @Override
    public DeleteEntityMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        return new DeleteEntityMessage(id, key);
    }
}
