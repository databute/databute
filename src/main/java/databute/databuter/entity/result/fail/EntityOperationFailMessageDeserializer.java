package databute.databuter.entity.result.fail;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationFailMessageDeserializer implements MessageDeserializer<EntityOperationFailMessage> {

    @Override
    public EntityOperationFailMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntityOperationErrorCode errorCode = EntityOperationErrorCode.valueOf(packet.readString());
        return new EntityOperationFailMessage(id, key, errorCode);
    }
}
