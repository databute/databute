package databute.databuter.entry.set;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessageSerializer implements MessageSerializer<SetEntityMessage> {

    @SuppressWarnings("unchecked")
    @Override
    public Packet serialize(SetEntityMessage setEntityMessage) {
        checkNotNull(setEntityMessage, "setEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(setEntityMessage.id());
        packet.writeString(setEntityMessage.key());
        packet.writeString(setEntityMessage.valueType().name());
        switch (setEntityMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) setEntityMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) setEntityMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) setEntityMessage.value();
                serializeStringValue(packet, stringValue);
            }
            case LIST: {
                final List<String> listValue = (List<String>) setEntityMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) setEntityMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) setEntityMessage.value();
                serializeDictionaryValue(packet, dictionaryValue);
                break;
            }
        }
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
