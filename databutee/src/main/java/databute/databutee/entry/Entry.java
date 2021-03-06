package databute.databutee.entry;

import com.google.common.base.MoreObjects;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class Entry {

    private final EntryKey key;
    private final Object value;
    private final Instant createdTimestamp;
    private final Instant lastUpdatedTimestamp;

    public Entry(EntryKey key, Object value, Instant createdTimestamp, Instant lastUpdatedTimestamp) {
        this.key = checkNotNull(key, "key");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
    }

    public EntryKey key() {
        return key;
    }

    public <T> T value(Class<T> tClass) {
        if (tClass.isInstance(value)) {
            return tClass.cast(value);
        } else {
            throw new ClassCastException();
        }
    }

    public Integer valueAsInteger() {
        return value(Integer.class);
    }

    public Long valueAsLong() {
        return value(Long.class);
    }

    public String valueAsString() {
        return value(String.class);
    }

    public Instant createdTimestamp() {
        return createdTimestamp;
    }

    public Instant lastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("value", value)
                .add("createdTimestamp", createdTimestamp)
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp)
                .toString();
    }
}
