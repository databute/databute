package databute.databuter.entity.result.success;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationSuccessMessageSerializer implements MessageSerializer<EntityOperationSuccessMessage> {

    @SuppressWarnings("unchecked")
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
                break;
            }
            case LIST: {
                final List<String> listValue = (List<String>) entityOperationSuccessMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) entityOperationSuccessMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) entityOperationSuccessMessage.value();
                serializeDictionaryValue(packet, dictionaryValue);
                break;
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

    private void serializeListValue(Packet packet, List<String> listValue) {
        packet.writeInt(listValue.size());
        listValue.forEach(packet::writeString);
    }

    private void serializeSetValue(Packet packet, Set<String> setValue) {
        packet.writeInt(setValue.size());
        setValue.forEach(packet::writeString);
    }

    private void serializeDictionaryValue(Packet packet, Map<String, String> dictionaryValue) {
        packet.writeInt(dictionaryValue.size());
        dictionaryValue.forEach((itemKey, item) -> {
            packet.writeString(itemKey);
            packet.writeString(item);
        });
    }
}
