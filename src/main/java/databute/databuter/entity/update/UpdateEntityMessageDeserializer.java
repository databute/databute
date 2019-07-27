package databute.databuter.entity.update;

import databute.databuter.entity.EntityValueType;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntityMessageDeserializer implements MessageDeserializer<UpdateEntityMessage> {

    @Override
    public UpdateEntityMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntityValueType valueType = EntityValueType.valueOf(packet.readString());
        switch (valueType) {
            case INTEGER: {
                final Integer integerValue = deserializeIntegerValue(packet);
                return new UpdateEntityMessage(id, key, valueType, integerValue);
            }
            case LONG: {
                final Long longValue = deserializeLongValue(packet);
                return new UpdateEntityMessage(id, key, valueType, longValue);
            }
            case STRING: {
                final String stringValue = deserializeStringValue(packet);
                return new UpdateEntityMessage(id, key, valueType, stringValue);
            }
        }

        throw new UnsupportedValueTypeException();
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
