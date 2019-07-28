package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;
import databute.databuter.entity.EntityKey;

import java.time.Instant;

public class StringEntity extends AbstractEntity<String> {

    public StringEntity(EntityKey key, String value) {
        super(key, value);
    }

    public StringEntity(EntityKey key, String value, Instant createdTimestamp, Instant lastUpdatedTimestamp) {
        super(key, value, createdTimestamp, lastUpdatedTimestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key())
                .add("value", value())
                .add("createdTimestamp", createdTimestamp())
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp())
                .toString();
    }
}
