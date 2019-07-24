package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.cluster.ClusterConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DatabuterConfiguration {

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private int port;

    @JsonProperty("cluster")
    private ClusterConfiguration cluster;

    public String address() {
        return address;
    }

    public int port() {
        return port;
    }

    public ClusterConfiguration cluster() {
        return cluster;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("address", address)
                .add("port", port)
                .add("cluster", cluster)
                .toString();
    }
}
