package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.cluster.ClusterConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DatabuterConfiguration {

    @JsonProperty("cluster")
    private ClusterConfiguration cluster;

    public ClusterConfiguration cluster() {
        return cluster;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cluster", cluster)
                .toString();
    }
}
