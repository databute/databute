package databute.databuter.bucket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.net.InetSocketAddress;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BucketConfiguration {

    @JsonProperty("id")
    private String id;

    @JsonProperty("masterClusterId")
    private String masterClusterId;

    @JsonProperty("backupClusterId")
    private String backupClusterId;

    public String id() {
        return id;
    }

    public String masterClusterId() {
        return masterClusterId;
    }

    public String backupClusterId() {
        return backupClusterId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("masterClusterId", masterClusterId)
                .add("backupClusterId",backupClusterId)
                .toString();
    }
}
