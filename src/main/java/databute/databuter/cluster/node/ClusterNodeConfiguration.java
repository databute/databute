package databute.databuter.cluster.node;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNodeConfiguration {

    public static ClusterNodeConfiguration.Builder builder() {
        return new ClusterNodeConfiguration.Builder();
    }

    private final String id;
    private final String address;
    private final int port;

    private ClusterNodeConfiguration(String id, String address, int port) {
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

    public static class Builder {

        private String id;
        private String address;
        private int port;

        private Builder() {
            super();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public ClusterNodeConfiguration build() {
            return new ClusterNodeConfiguration(id, address, port);
        }
    }
}
