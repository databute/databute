package databute.databuter.entry.result.success;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.Entry;
import databute.databuter.entry.EntryMessage;
import databute.databuter.entry.EntryValueType;
import databute.databuter.entry.UnsupportedValueTypeException;
import databute.databuter.entry.type.*;
import databute.network.message.MessageCode;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationSuccessMessage implements EntryMessage {

    public static EntryOperationSuccessMessage entry(String id, Entry entry) {
        checkNotNull(entry, "entry");

        if (entry instanceof IntegerEntry) {
            final IntegerEntry integerEntry = (IntegerEntry) entry;
            return new EntryOperationSuccessMessage(id, integerEntry.key().key(),
                    EntryValueType.INTEGER, integerEntry.value(),
                    integerEntry.createdTimestamp(), integerEntry.lastUpdatedTimestamp(),
                    integerEntry.expirationTimestamp());
        } else if (entry instanceof LongEntry) {
            final LongEntry longEntry = (LongEntry) entry;
            return new EntryOperationSuccessMessage(id, longEntry.key().key(),
                    EntryValueType.LONG, longEntry.value(),
                    longEntry.createdTimestamp(), longEntry.lastUpdatedTimestamp(),
                    longEntry.expirationTimestamp());
        } else if (entry instanceof StringEntry) {
            final StringEntry stringEntry = (StringEntry) entry;
            return new EntryOperationSuccessMessage(id, stringEntry.key().key(),
                    EntryValueType.STRING, stringEntry.value(),
                    stringEntry.createdTimestamp(), stringEntry.lastUpdatedTimestamp(),
                    stringEntry.expirationTimestamp());
        } else if (entry instanceof ListEntry) {
            final ListEntry listEntry = (ListEntry) entry;
            return new EntryOperationSuccessMessage(id, listEntry.key().key(),
                    EntryValueType.LIST, listEntry.value(),
                    listEntry.createdTimestamp(), listEntry.lastUpdatedTimestamp(),
                    listEntry.expirationTimestamp());
        } else if (entry instanceof SetEntry) {
            final SetEntry setEntry = (SetEntry) entry;
            return new EntryOperationSuccessMessage(id, setEntry.key().key(),
                    EntryValueType.SET, setEntry.value(),
                    setEntry.createdTimestamp(), setEntry.lastUpdatedTimestamp(),
                    setEntry.expirationTimestamp());
        } else if (entry instanceof DictionaryEntry) {
            final DictionaryEntry dictionaryEntry = (DictionaryEntry) entry;
            return new EntryOperationSuccessMessage(id, dictionaryEntry.key().key(),
                    EntryValueType.DICTIONARY, dictionaryEntry.value(),
                    dictionaryEntry.createdTimestamp(), dictionaryEntry.lastUpdatedTimestamp(),
                    dictionaryEntry.expirationTimestamp());
        } else {
            throw new UnsupportedValueTypeException();
        }
    }

    private final String id;
    private final String key;
    private final EntryValueType valueType;
    private final Object value;
    private final Instant createdTimestamp;
    private final Instant lastUpdatedTimestamp;
    private final Instant expirationTimestamp;

    public EntryOperationSuccessMessage(String id,
                                        String key,
                                        EntryValueType valueType,
                                        Object value,
                                        Instant createdTimestamp,
                                        Instant lastUpdatedTimestamp,
                                        Instant expirationTimestamp) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
        this.expirationTimestamp = expirationTimestamp;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTRY_OPERATION_SUCCESS;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public EntryValueType valueType() {
        return valueType;
    }

    public Object value() {
        return value;
    }

    public Instant createdTimestamp() {
        return createdTimestamp;
    }

    public Instant lastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public Instant expirationTimestamp() {
        return expirationTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .add("createdTimestamp", createdTimestamp)
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp)
                .add("expirationTimestamp", expirationTimestamp)
                .toString();
    }
}
