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

    @JsonProperty("masterNodeId")
    private String masterNodeId;

    @JsonProperty("backupNodeId")
    private String backupNodeId;

    public BucketConfiguration() {
        this.id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }

    public String masterNodeId() {
        return masterNodeId;
    }

    public BucketConfiguration masterNodeId(String masterNodeId) {
        this.masterNodeId = masterNodeId;
        return this;
    }

    public String backupNodeId() {
        return backupNodeId;
    }

    public BucketConfiguration backupNodeId(String backupNodeId) {
        this.backupNodeId = backupNodeId;
        return this;
    }

    public void update(BucketConfiguration other) {
        if (!StringUtils.equals(masterNodeId, other.masterNodeId())) {
            this.masterNodeId = other.masterNodeId();
        }

        if (!StringUtils.equals(backupNodeId, other.backupNodeId())) {
            this.backupNodeId = other.backupNodeId();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("masterNodeId", masterNodeId)
                .add("backupNodeId", backupNodeId)
                .toString();
    }
}
