package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;
import databute.databuter.entity.EntityKey;

import java.time.Instant;

public class IntegerEntity extends AbstractEntity<Integer> {

    public IntegerEntity(EntityKey key, Integer value) {
        super(key, value);
    }

    public IntegerEntity(EntityKey key, Integer value, Instant createdTimestamp, Instant lastUpdatedTimestamp) {
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
