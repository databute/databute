package databute.databuter.entry.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.AbstractEntry;
import databute.databuter.entry.EntryKey;

import java.time.Instant;
import java.util.Map;

public class DictionaryEntry extends AbstractEntry<Map<String, String>> {

    public DictionaryEntry(EntryKey key, Map<String, String> value) {
        super(key, value);
    }

    public DictionaryEntry(EntryKey key,
                           Map<String, String> value,
                           Instant createdTimestamp,
                           Instant lastUpdatedTimestamp,
                           Instant expirationTimestamp) {
        super(key, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key())
                .add("value", value())
                .add("createdTimestamp", createdTimestamp())
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp())
                .add("expirationTimestamp", expirationTimestamp())
                .toString();
    }
}
