package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.client.ClientConfiguration;
import databute.databuter.cluster.ClusterConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DatabuterConfiguration {

    @JsonProperty("guardMemorySize")
    private int guardMemorySize;

    @JsonProperty("bucketMemorySize")
    private int bucketMemorySize;

    @JsonProperty("client")
    private ClientConfiguration client;

    @JsonProperty("cluster")
    private ClusterConfiguration cluster;

    public int guardMemorySizeMb() {
        return guardMemorySize * 1024 * 1024;
    }

    public int bucketMemorySizeMb() {
        return bucketMemorySize * 1024 * 1024;
    }

    public ClientConfiguration client() {
        return client;
    }

    public ClusterConfiguration cluster() {
        return cluster;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketMemorySize", bucketMemorySize)
                .add("gaurdMemorySize", guardMemorySize)
                .add("client", client)
                .add("cluster", cluster)
                .toString();
    }
}
