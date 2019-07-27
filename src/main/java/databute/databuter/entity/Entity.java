package databute.databuter.entity;

import java.time.Instant;

public interface Entity<T> {

    String key();

    T value();

    T set(T value);

    Instant createdTimestamp();

    Instant lastUpdatedTimestamp();

}
