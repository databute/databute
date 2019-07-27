package databute.databuter.entity.result.fail;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationFailMessageSerializer implements MessageSerializer<EntityOperationFailMessage> {

    @Override
    public Packet serialize(EntityOperationFailMessage entityOperationFailMessage) {
        checkNotNull(entityOperationFailMessage, "entityOperationFailMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(entityOperationFailMessage.id());
        packet.writeString(entityOperationFailMessage.key());
        packet.writeString(entityOperationFailMessage.errorCode().name());
        return packet;
    }
}
