package databute.databuter.entity.result.success;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationSuccessMessageSerializer implements MessageSerializer<EntityOperationSuccessMessage> {

    @Override
    public Packet serialize(EntityOperationSuccessMessage entityOperationSuccessMessage) {
        checkNotNull(entityOperationSuccessMessage, "entityOperationSuccessMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(entityOperationSuccessMessage.id());
        packet.writeString(entityOperationSuccessMessage.key());
        packet.writeString(entityOperationSuccessMessage.valueType().name());
        switch (entityOperationSuccessMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) entityOperationSuccessMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) entityOperationSuccessMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) entityOperationSuccessMessage.value();
                serializeStringValue(packet, stringValue);
            }
        }
        packet.writeLong(entityOperationSuccessMessage.createdTimestamp().toEpochMilli());
        packet.writeLong(entityOperationSuccessMessage.lastUpdatedTimestamp().toEpochMilli());
        return packet;
    }

    private void serializeIntegerValue(Packet packet, Integer integerValue) {
        packet.writeInt(integerValue);
    }

    private void serializeLongValue(Packet packet, Long longValue) {
        packet.writeLong(longValue);
    }

    private void serializeStringValue(Packet packet, String stringValue) {
        packet.writeString(stringValue);
    }
}
