package databute.databuter.entry.result.success;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationSuccessMessageSerializer implements MessageSerializer<EntryOperationSuccessMessage> {

    @SuppressWarnings("unchecked")
    @Override
    public Packet serialize(EntryOperationSuccessMessage entryOperationSuccessMessage) {
        checkNotNull(entryOperationSuccessMessage, "entryOperationSuccessMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(entryOperationSuccessMessage.id());
        packet.writeString(entryOperationSuccessMessage.key());
        packet.writeString(entryOperationSuccessMessage.valueType().name());
        switch (entryOperationSuccessMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) entryOperationSuccessMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) entryOperationSuccessMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) entryOperationSuccessMessage.value();
                serializeStringValue(packet, stringValue);
                break;
            }
            case LIST: {
                final List<String> listValue = (List<String>) entryOperationSuccessMessage.value();
                serializeListValue(packet, listValue);
                break;
            }
            case SET: {
                final Set<String> setValue = (Set<String>) entryOperationSuccessMessage.value();
                serializeSetValue(packet, setValue);
                break;
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = (Map<String, String>) entryOperationSuccessMessage.value();
                serializeDictionaryValue(packet, dictionaryValue);
                break;
            }
        }
        packet.writeLong(entryOperationSuccessMessage.createdTimestamp().toEpochMilli());
        packet.writeLong(entryOperationSuccessMessage.lastUpdatedTimestamp().toEpochMilli());
        if (entryOperationSuccessMessage.expirationTimestamp() == null) {
            packet.writeBoolean(false);
        } else {
            packet.writeBoolean(true);
            packet.writeLong(entryOperationSuccessMessage.expirationTimestamp().toEpochMilli());
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
