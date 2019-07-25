package databute.databuter.client.cluster.add;

import com.google.common.base.MoreObjects;
import databute.databuter.client.network.ClientMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddClusterNodeMessage implements Message {

    public static AddClusterNodeMessage.Builder builder() {
        return new AddClusterNodeMessage.Builder();
    }

    private final String id;
    private final String address;
    private final int port;

    public AddClusterNodeMessage(String id, String address, int port) {
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    @Override
    public MessageCode messageCode() {
        return ClientMessageCode.ADD_CLUSTER_NODE;
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
                .add("messageCode", messageCode())
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

        public AddClusterNodeMessage.Builder id(String id) {
            this.id = id;
            return this;
        }

        public AddClusterNodeMessage.Builder address(String address) {
            this.address = address;
            return this;
        }

        public AddClusterNodeMessage.Builder port(int port) {
            this.port = port;
            return this;
        }

        public AddClusterNodeMessage build() {
            return new AddClusterNodeMessage(id, address, port);
        }
    }
}
