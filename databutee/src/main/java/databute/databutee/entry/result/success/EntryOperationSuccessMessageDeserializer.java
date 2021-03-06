package databute.databutee.entry.result.success;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import databute.databutee.entry.EntryValueType;
import databute.databutee.entry.UnsupportedValueTypeException;
import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationSuccessMessageDeserializer implements MessageDeserializer<EntryOperationSuccessMessage> {

    @Override
    public EntryOperationSuccessMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntryValueType valueType = EntryValueType.valueOf(packet.readString());
        Object value = null;
        switch (valueType) {
            case INTEGER: {
                value = deserializeIntegerValue(packet);
                break;
            }
            case LONG: {
                value = deserializeLongValue(packet);
                break;
            }
            case STRING: {
                value = deserializeStringValue(packet);
                break;
            }
            case LIST: {
                value = deserializeListValue(packet);
                break;
            }
            case SET: {
                value = deserializeSetValue(packet);
                break;
            }
            case DICTIONARY: {
                value = deserializeDictionaryValue(packet);
                break;
            }
            default:
                throw new UnsupportedValueTypeException();
        }
        final Instant createdTimestamp = Instant.ofEpochMilli(packet.readLong());
        final Instant lastUpdatedTimestamp = Instant.ofEpochMilli(packet.readLong());
        return new EntryOperationSuccessMessage(id, key, valueType, value, createdTimestamp, lastUpdatedTimestamp);
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
