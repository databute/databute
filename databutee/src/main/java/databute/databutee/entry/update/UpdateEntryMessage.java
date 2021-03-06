package databute.databutee.entry.update;

import com.google.common.base.MoreObjects;
import databute.databutee.entry.EntryKey;
import databute.databutee.entry.EntryMessage;
import databute.databutee.entry.EntryValueType;
import databute.databutee.network.message.MessageCode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntryMessage implements EntryMessage {

    public static UpdateEntryMessage updateInteger(EntryKey key, Integer integerValue) {
        return new UpdateEntryMessage(key, EntryValueType.INTEGER, integerValue);
    }

    public static UpdateEntryMessage updateLong(EntryKey key, Long longValue) {
        return new UpdateEntryMessage(key, EntryValueType.LONG, longValue);
    }

    public static UpdateEntryMessage updateString(EntryKey key, String stringValue) {
        return new UpdateEntryMessage(key, EntryValueType.STRING, stringValue);
    }

    public static UpdateEntryMessage updateList(EntryKey key, List<String> listValue) {
        return new UpdateEntryMessage(key, EntryValueType.LIST, listValue);
    }

    public static UpdateEntryMessage updateSet(EntryKey key, Set<String> setValue) {
        return new UpdateEntryMessage(key, EntryValueType.SET, setValue);
    }

    public static UpdateEntryMessage updateDictionary(EntryKey key, Map<String, String> dictionaryValue) {
        return new UpdateEntryMessage(key, EntryValueType.DICTIONARY, dictionaryValue);
    }

    private final String id;
    private final EntryKey key;
    private final EntryValueType valueType;
    private final Object value;

    public UpdateEntryMessage(EntryKey key, EntryValueType valueType, Object value) {
        this.id = UUID.randomUUID().toString();
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.UPDATE_ENTRY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key.key();
    }

    public EntryValueType valueType() {
        return valueType;
    }

    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
