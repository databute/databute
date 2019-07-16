package databute.databuter.cluster;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNode {

    private final String id;
    private final String address;
    private final int port;

    public ClusterNode(String id, String address, int port) {
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    public String id() {
        return id;
    }

    public String address() {
        return address;
    }

    public int port() {
        return port;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }
}
