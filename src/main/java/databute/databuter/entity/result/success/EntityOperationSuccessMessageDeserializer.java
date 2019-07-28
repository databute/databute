package databute.databuter.entity.result.success;

import databute.databuter.entity.EntityValueType;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationSuccessMessageDeserializer implements MessageDeserializer<EntityOperationSuccessMessage> {

    @Override
    public EntityOperationSuccessMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntityValueType valueType = EntityValueType.valueOf(packet.readString());
        Object value = null;
        switch (valueType) {
            case INTEGER: {
                value = deserializeIntegerValue(packet);
                break;
            }
            case LONG: {
                value = deserializeLongValue(packet);
                break;
            }
            case STRING: {
                value = deserializeStringValue(packet);
                break;
            }
            default:
                throw new UnsupportedValueTypeException();
        }
        final Instant createdTimestamp = Instant.ofEpochMilli(packet.readLong());
        final Instant lastUpdatedTimestamp = Instant.ofEpochMilli(packet.readLong());
        return new EntityOperationSuccessMessage(id, key, valueType, value, createdTimestamp, lastUpdatedTimestamp);
    }

    private Integer deserializeIntegerValue(Packet packet) {
        return packet.readInt();
    }

    private Long deserializeLongValue(Packet packet) {
        return packet.readLong();
    }

    private String deserializeStringValue(Packet packet) {
        return packet.readString();
    }
}
