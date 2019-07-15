package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.MoreObjects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DatabuterConfiguration {

    private String foo;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("foo", foo)
                .toString();
    }
}
