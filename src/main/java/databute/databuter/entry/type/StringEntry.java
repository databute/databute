package databute.databuter.entry.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.AbstractEntry;
import databute.databuter.entry.EntryKey;

import java.time.Instant;

public class StringEntry extends AbstractEntry<String> {

    public StringEntry(EntryKey key, String value) {
        super(key, value);
    }

    public StringEntry(EntryKey key,
                       String value,
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
