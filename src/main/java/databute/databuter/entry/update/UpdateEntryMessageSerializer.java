package databute.databuter.entry.update;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntryMessageSerializer implements MessageSerializer<UpdateEntryMessage> {

    @SuppressWarnings("unchecked")
    @Override
    public Packet serialize(UpdateEntryMessage updateEntryMessage) {
        checkNotNull(updateEntryMessage, "updateEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(updateEntryMessage.id());
        packet.writeString(updateEntryMessage.key());
        packet.writeString(updateEntryMessage.valueType().name());
        switch (updateEntryMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) updateEntryMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) updateEntryMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) updateEntryMessage.value();
                serializeStringValue(packet, stringValue);
            }
            case LIST: {
                final List<String> listValue = (List<String>) updateEntryMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) updateEntryMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) updateEntryMessage.value();
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
