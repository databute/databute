package databute.databutee.entry;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

public class EntryKey {

    private final String key;

    public EntryKey(String key) throws EmptyEntryKeyException {
        if (StringUtils.isEmpty(key)) {
            throw new EmptyEntryKeyException(key);
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
        EntryKey entryKey = (EntryKey) o;
        return Objects.equal(key, entryKey.key);
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
