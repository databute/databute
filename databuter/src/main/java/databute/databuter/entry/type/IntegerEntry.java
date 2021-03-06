package databute.databuter.entry.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.AbstractEntry;
import databute.databuter.entry.EntryKey;

import java.time.Instant;

public class IntegerEntry extends AbstractEntry<Integer> {

    public IntegerEntry(EntryKey key, Integer value) {
        super(key, value);
    }

    public IntegerEntry(EntryKey key,
                        Integer value,
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
