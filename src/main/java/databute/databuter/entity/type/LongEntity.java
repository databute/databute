package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;

public class LongEntity extends AbstractEntity<Long> {

    public LongEntity(String key, Long value) {
        super(key, value);
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
