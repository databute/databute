package databute.databuter.cluster;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.cluster.coordinator.ClusterCoordinatorConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClusterConfiguration {

    @JsonProperty("coordinator")
    private ClusterCoordinatorConfiguration coordinator;

    public ClusterCoordinatorConfiguration coordinator() {
        return coordinator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("coordinator", coordinator)
                .toString();
    }
}
