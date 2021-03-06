package databute.databuter.entry;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractEntry<T> implements Entry<T> {

    private T value;
    private Instant lastUpdatedTimestamp;
    private Instant expirationTimestamp;

    private final EntryKey key;
    private final Instant createdTimestamp;
    private final ReentrantLock lock;

    protected AbstractEntry(EntryKey key, T value) {
        this(key, value, Instant.now());
    }

    protected AbstractEntry(EntryKey key, T value, Instant createdTimestamp) {
        this(key, value, createdTimestamp, createdTimestamp, null);
    }

    protected AbstractEntry(EntryKey key,
                            T value,
                            Instant createdTimestamp,
                            Instant lastUpdatedTimestamp,
                            Instant expirationTimestamp) {
        this.key = checkNotNull(key, "key");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp ");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
        this.expirationTimestamp = expirationTimestamp;
        this.lock = new ReentrantLock();
    }

    @Override
    public final EntryKey key() {
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

    @Override
    public void expireAt(Instant expirationTimestamp) {
        lock.lock();
        try {
            this.expirationTimestamp = expirationTimestamp;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelExpiration() {
        expireAt(null);
    }

    @Override
    public boolean willBeExpire() {
        return (expirationTimestamp != null);
    }

    @Override
    public final Instant expirationTimestamp() {
        return expirationTimestamp;
    }
}
