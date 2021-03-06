package databute.databuter.entry.update;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import databute.databuter.entry.EntryValueType;
import databute.databuter.entry.UnsupportedValueTypeException;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntryMessageDeserializer implements MessageDeserializer<UpdateEntryMessage> {

    @Override
    public UpdateEntryMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntryValueType valueType = EntryValueType.valueOf(packet.readString());
        switch (valueType) {
            case INTEGER: {
                final Integer integerValue = deserializeIntegerValue(packet);
                return new UpdateEntryMessage(id, key, valueType, integerValue);
            }
            case LONG: {
                final Long longValue = deserializeLongValue(packet);
                return new UpdateEntryMessage(id, key, valueType, longValue);
            }
            case STRING: {
                final String stringValue = deserializeStringValue(packet);
                return new UpdateEntryMessage(id, key, valueType, stringValue);
            }
            case LIST: {
                final List<String> listValue = deserializeListValue(packet);
                return new UpdateEntryMessage(id, key, valueType, listValue);
            }
            case SET: {
                final Set<String> setValue = deserializeSetValue(packet);
                return new UpdateEntryMessage(id, key, valueType, setValue);
            }
            case DICTIONARY: {
                final Map<String, String> dictionaryValue = deserializeDictionaryValue(packet);
                return new UpdateEntryMessage(id, key, valueType, dictionaryValue);
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

    private List<String> deserializeListValue(Packet packet) {
        final List<String> value = Lists.newArrayList();

        final int length = packet.readInt();
        for (int i = 0; i < length; i++) {
            final String item = packet.readString();
            value.add(item);
        }

        return value;
    }

    private Set<String> deserializeSetValue(Packet packet) {
        final Set<String> value = Sets.newHashSet();

        final int length = packet.readInt();
        for (int i = 0; i < length; i++) {
            final String item = packet.readString();
            value.add(item);
        }

        return value;
    }

    private Map<String, String> deserializeDictionaryValue(Packet packet) {
        final Map<String, String> value = Maps.newHashMap();

        final int length = packet.readInt();
        for (int i = 0; i < length; i++) {
            final String itemKey = packet.readString();
            final String item = packet.readString();
            value.put(itemKey, item);
        }

        return value;
    }
}
