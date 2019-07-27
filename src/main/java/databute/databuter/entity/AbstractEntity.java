package databute.databuter.entity;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractEntity<T> implements Entity<T> {

    private T value;
    private Instant lastUpdatedTimestamp;

    private final String key;
    private final Instant createdTimestamp;

    protected AbstractEntity(String key, T value) {
        this.key = checkNotNull(key, "key");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = Instant.now();
        this.lastUpdatedTimestamp = createdTimestamp;
    }

    @Override
    public final String key() {
        return key;
    }

    @Override
    public final T value() {
        return value;
    }

    @Override
    public final T set(T value) {
        this.value = checkNotNull(value, "value");
        this.lastUpdatedTimestamp = Instant.now();
        return this.value;
    }

    @Override
    public final Instant createdTimestamp() {
        return createdTimestamp;
    }

    @Override
    public final Instant lastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }
}
