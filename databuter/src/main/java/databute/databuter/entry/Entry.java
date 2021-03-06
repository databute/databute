package databute.databuter.entry;

import java.time.Instant;

public interface Entry<T> {

    EntryKey key();

    T value();

    T set(T value);

    Instant createdTimestamp();

    Instant lastUpdatedTimestamp();

    void expireAt(Instant expirationTimestamp);

    void cancelExpiration();

    boolean willBeExpire();

    Instant expirationTimestamp();

}
