package databute.databuter.entity.request;

import databute.databuter.entity.EntityType;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityRequestMessageDeserializer implements MessageDeserializer<EntityRequestMessage> {

    @Override
    public EntityRequestMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final EntityRequestType requestType = EntityRequestType.valueOf(packet.readString());
        final String key = packet.readString();
        final EntityType valueType = EntityType.valueOf(packet.readString());
        switch (valueType) {
            case INTEGER: {
                final Integer value = packet.readInt();
                return new EntityRequestMessage(requestType, key, valueType, value);
            }
            case LONG: {
                final Long value = packet.readLong();
                return new EntityRequestMessage(requestType, key, valueType, value);
            }
            case STRING: {
                final String value = packet.readString();
                return new EntityRequestMessage(requestType, key, valueType, value);
            }
        }

        final String value = packet.readString();
        return new EntityRequestMessage(requestType, key, EntityType.STRING, value);
    }
}
