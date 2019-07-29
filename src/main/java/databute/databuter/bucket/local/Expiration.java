package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import databute.databuter.entity.EntityKey;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class Expiration implements Comparable<Expiration> {

    private final EntityKey key;
    private final Instant expireTimestamp;

    public Expiration(EntityKey key, Instant expireTimestamp) {
        this.key = checkNotNull(key, "key");
        this.expireTimestamp = checkNotNull(expireTimestamp, "expireTimestamp");
    }

    public EntityKey key() {
        return key;
    }

    public Instant expireTimestamp() {
        return expireTimestamp;
    }

    @Override
    public int compareTo(Expiration e) {
        return this.expireTimestamp.isBefore(e.expireTimestamp()) ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expiration that = (Expiration) o;
        return (Objects.equal(key, that.key) && Objects.equal(expireTimestamp, that.expireTimestamp));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, expireTimestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("expireTimestamp", expireTimestamp)
                .toString();
    }
}
