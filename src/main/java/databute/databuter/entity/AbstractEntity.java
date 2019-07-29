package databute.databuter.entity;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractEntity<T> implements Entity<T> {

    private T value;
    private Instant lastUpdatedTimestamp;

    private final EntityKey key;
    private final Instant createdTimestamp;
    private final ReentrantLock lock;

    protected AbstractEntity(EntityKey key, T value) {
        this(key, value, Instant.now());
    }

    protected AbstractEntity(EntityKey key, T value, Instant createdTimestamp) {
        this(key, value, createdTimestamp, createdTimestamp);
    }

    protected AbstractEntity(EntityKey key, T value, Instant createdTimestamp, Instant lastUpdatedTimestamp) {
        this.key = checkNotNull(key, "key");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp ");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
        this.lock = new ReentrantLock();
    }

    @Override
    public final EntityKey key() {
        return key;
    }

    @Override
    public final T value() {
        lock.lock();
        try {
            return this.value;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final T set(T value) {
        checkNotNull(value, "value");

        lock.lock();
        try {
            this.value = value;
            this.lastUpdatedTimestamp = Instant.now();
            return this.value;
        } finally {
            lock.unlock();
        }
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
