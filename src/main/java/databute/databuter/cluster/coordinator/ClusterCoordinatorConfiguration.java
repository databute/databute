package databute.databuter.cluster.coordinator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClusterCoordinatorConfiguration {

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private int port;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("address", address)
                .add("port", port)
                .toString();
    }
}
