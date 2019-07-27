package databute.databuter.entity;

import java.time.Instant;

public interface Entity<T> {

    EntityKey key();

    T value();

    T set(T value);

    Instant createdTimestamp();

    Instant lastUpdatedTimestamp();

}
