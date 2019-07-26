package databute.databuter.bucket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BucketConfiguration {

    @JsonProperty("id")
    private String id;

    @JsonProperty("masterClusterId")
    private String masterClusterId;

    @JsonProperty("backupClusterId")
    private String backupClusterId;

    public BucketConfiguration() {
        this.id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }

    public String masterClusterId() {
        return masterClusterId;
    }

    public BucketConfiguration masterClusterId(String masterClusterId) {
        this.masterClusterId = masterClusterId;
        return this;
    }

    public String backupClusterId() {
        return backupClusterId;
    }

    public BucketConfiguration backupClusterId(String backupClusterId) {
        this.backupClusterId = backupClusterId;
        return this;
    }

    public void update(BucketConfiguration other) {
        if (!StringUtils.equals(masterClusterId, other.masterClusterId())) {
            this.masterClusterId = other.masterClusterId();
        }

        if (!StringUtils.equals(backupClusterId, other.backupClusterId())) {
            this.backupClusterId = other.backupClusterId();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("masterClusterId", masterClusterId)
                .add("backupClusterId", backupClusterId)
                .toString();
    }
}
