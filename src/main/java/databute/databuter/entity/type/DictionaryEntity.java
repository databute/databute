package databute.databuter.entity.type;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.AbstractEntity;
import databute.databuter.entity.EntityKey;

import java.time.Instant;
import java.util.Map;

public class DictionaryEntity extends AbstractEntity<Map<String, String>> {

    public DictionaryEntity(EntityKey key, Map<String, String> value) {
        super(key, value);
    }

    public DictionaryEntity(EntityKey key,
                            Map<String, String> value,
                            Instant createdTimestamp,
                            Instant lastUpdatedTimestamp,
                            Instant expirationTimestamp) {
        super(key, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key())
                .add("value", value())
                .add("createdTimestamp", createdTimestamp())
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp())
                .add("expirationTimestamp", expirationTimestamp())
                .toString();
    }
}
