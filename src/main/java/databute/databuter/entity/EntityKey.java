package databute.databuter.entity;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

public class EntityKey {

    private final String key;

    public EntityKey(String key) throws EmptyEntityKeyException {
        if (StringUtils.isEmpty(key)) {
            throw new EmptyEntityKeyException(key);
        }
        this.key = key;
    }

    public String key() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityKey entityKey = (EntityKey) o;
        return Objects.equal(key, entityKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
