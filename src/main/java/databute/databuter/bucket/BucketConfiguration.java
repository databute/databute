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

    public String activeNodeId() {
        return activeNodeId;
    }

    public BucketConfiguration activeNodeId(String activeNodeId) {
        this.activeNodeId = activeNodeId;
        return this;
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    public BucketConfiguration standbyNodeId(String standbyNodeId) {
        this.standbyNodeId = standbyNodeId;
        return this;
    }

    public void update(BucketConfiguration other) {
        if (!StringUtils.equals(activeNodeId, other.activeNodeId())) {
            this.activeNodeId = other.activeNodeId();
        }

        if (!StringUtils.equals(standbyNodeId, other.standbyNodeId())) {
            this.standbyNodeId = other.standbyNodeId();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
