package databute.databuter.entity.set;

import databute.databuter.entity.EntityValueType;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessageDeserializer implements MessageDeserializer<SetEntityMessage> {

    @Override
    public SetEntityMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String key = packet.readString();
        final EntityValueType valueType = EntityValueType.valueOf(packet.readString());
        switch (valueType) {
            case INTEGER: {
                final Integer integerValue = deserializeIntegerValue(packet);
                return new SetEntityMessage(key, valueType, integerValue);
            }
            case LONG: {
                final Long longValue = deserializeLongValue(packet);
                return new SetEntityMessage(key, valueType, longValue);
            }
            case STRING: {
                final String stringValue = deserializeStringValue(packet);
                return new SetEntityMessage(key, valueType, stringValue);
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
