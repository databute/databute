package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ZooKeeperConfiguration {

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private int port;

    @JsonProperty("baseSleepTimeMs")
    private int baseSleepTimeMs;

    @JsonProperty("maxRetries")
    private int maxRetries;

    public String connectString() {
        return StringUtils.joinWith(":", address, port);
    }

    public int baseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public int maxRetries() {
        return maxRetries;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("address", address)
                .add("port", port)
                .add("baseSleepTimeMs", baseSleepTimeMs)
                .add("maxRetries", maxRetries)
                .toString();
    }
}
