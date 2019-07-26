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

    @JsonProperty("factor")
    private int factor;

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

    public int factor() {
        return factor;
    }

    public BucketConfiguration factor(int factor) {
        this.factor = factor;
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

        if (factor != other.factor()) {
            this.factor = other.factor();
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
                .add("factor", factor)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
