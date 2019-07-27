package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;

public class IntegerEntity extends AbstractEntity<Integer> {

    public IntegerEntity(String key, Integer value) {
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
