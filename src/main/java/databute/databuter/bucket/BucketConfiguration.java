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

    @JsonProperty("keyFactor")
    private int keyFactor;

    @JsonProperty("activeNodeId")
    private String activeNodeId;

    @JsonProperty("standbyNodeId")
    private String standbyNodeId;

    public BucketConfiguration() {
        this.id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }

    public int keyFactor() {
        return keyFactor;
    }

    public BucketConfiguration keyFactor(int keyFactor) {
        this.keyFactor = keyFactor;
        return this;
    }

    public String activeNodeId() {
        return activeNodeId;
    }

    public BucketConfiguration activeNodeId(String activeNodeId) {
        this.activeNodeId = activeNodeId;
        return this;
    }

    public boolean isActiveBy(String nodeId) {
        return StringUtils.equals(activeNodeId, nodeId);
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    public BucketConfiguration standbyNodeId(String standbyNodeId) {
        this.standbyNodeId = standbyNodeId;
        return this;
    }

    public boolean isStandbyBy(String nodeId) {
        return StringUtils.equals(standbyNodeId, nodeId);
    }

    public boolean update(BucketConfiguration other) {
        boolean updated = false;

        if (keyFactor != other.keyFactor()) {
            this.keyFactor = other.keyFactor();
            updated = true;
        }

        if (!StringUtils.equals(activeNodeId, other.activeNodeId())) {
            this.activeNodeId = other.activeNodeId();
            updated = true;
        }

        if (!StringUtils.equals(standbyNodeId, other.standbyNodeId())) {
            this.standbyNodeId = other.standbyNodeId();
            updated = true;
        }

        return updated;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("keyFactor", keyFactor)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
