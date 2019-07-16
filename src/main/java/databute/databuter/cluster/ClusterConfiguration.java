package databute.databuter.cluster;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.cluster.coordinator.ClusterCoordinatorConfiguration;

import java.net.InetSocketAddress;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClusterConfiguration {

    @JsonProperty("address")
    private String address;

    @JsonProperty("port")
    private int port;

    @JsonProperty("coordinator")
    private ClusterCoordinatorConfiguration coordinator;

    public String address() {
        return address;
    }

    public int port() {
        return port;
    }

    public InetSocketAddress localAddress() {
        return new InetSocketAddress(address, port);
    }

    public ClusterCoordinatorConfiguration coordinator() {
        return coordinator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("address", address)
                .add("port", port)
                .add("coordinator", coordinator)
                .toString();
    }
}
