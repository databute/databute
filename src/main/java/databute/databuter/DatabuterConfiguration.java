package databute.databuter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.client.ClientConfiguration;
import databute.databuter.cluster.ClusterConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DatabuterConfiguration {

    public static final long MEGABYTE = (1024 * 1024);

    @JsonProperty("guardMemorySize")
    private long guardMemorySize;

    @JsonProperty("bucketMemorySize")
    private long bucketMemorySize;

    @JsonProperty("client")
    private ClientConfiguration client;

    @JsonProperty("zooKeeper")
    private ZooKeeperConfiguration zooKeeper;

    @JsonProperty("cluster")
    private ClusterConfiguration cluster;

    public long guardMemorySize() {
        return (guardMemorySize * MEGABYTE);
    }

    public long bucketMemorySize() {
        return (bucketMemorySize * MEGABYTE);
    }

    public ClientConfiguration client() {
        return client;
    }

    public ZooKeeperConfiguration zooKeeper() {
        return zooKeeper;
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
                .add("zooKeeper", zooKeeper)
                .add("cluster", cluster)
                .toString();
    }
}
