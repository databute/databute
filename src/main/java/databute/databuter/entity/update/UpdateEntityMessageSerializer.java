package databute.databuter.entity.update;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntityMessageSerializer implements MessageSerializer<UpdateEntityMessage> {

    @SuppressWarnings("unchecked")
    @Override
    public Packet serialize(UpdateEntityMessage updateEntityMessage) {
        checkNotNull(updateEntityMessage, "updateEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(updateEntityMessage.id());
        packet.writeString(updateEntityMessage.key());
        packet.writeString(updateEntityMessage.valueType().name());
        switch (updateEntityMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) updateEntityMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) updateEntityMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) updateEntityMessage.value();
                serializeStringValue(packet, stringValue);
            }
            case LIST: {
                final List<String> listValue = (List<String>) updateEntityMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) updateEntityMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) updateEntityMessage.value();
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
