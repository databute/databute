package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;
import databute.databuter.entity.EntityKey;

import java.time.Instant;
import java.util.Set;

public class SetEntity extends AbstractEntity<Set<String>> {

    public SetEntity(EntityKey key, Set<String> value) {
        super(key, value);
    }

    public SetEntity(EntityKey key, Set<String> value, Instant createdTimestamp, Instant lastUpdatedTimestamp) {
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
