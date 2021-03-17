package databute.databuter.entry.set;

import databute.network.message.MessageSerializer;
import databute.network.packet.BufferedPacket;
import databute.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntryMessageSerializer implements MessageSerializer<SetEntryMessage> {

    @SuppressWarnings("unchecked")
    @Override
    public Packet serialize(SetEntryMessage setEntryMessage) {
        checkNotNull(setEntryMessage, "setEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(setEntryMessage.id());
        packet.writeString(setEntryMessage.key());
        packet.writeString(setEntryMessage.valueType().name());
        switch (setEntryMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) setEntryMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) setEntryMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) setEntryMessage.value();
                serializeStringValue(packet, stringValue);
                break;
            }
            case LIST: {
                final List<String> listValue = (List<String>) setEntryMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) setEntryMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) setEntryMessage.value();
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
