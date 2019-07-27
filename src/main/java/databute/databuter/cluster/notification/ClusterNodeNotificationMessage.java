package databute.databuter.cluster.notification;

import com.google.common.base.MoreObjects;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNodeNotificationMessage implements Message {

    public static ClusterNodeNotificationMessage.Builder added() {
        return new ClusterNodeNotificationMessage.Builder(ClusterNodeNotificationType.ADDED);
    }

    public static ClusterNodeNotificationMessage.Builder removed() {
        return new ClusterNodeNotificationMessage.Builder(ClusterNodeNotificationType.REMOVED);
    }

    private final ClusterNodeNotificationType type;
    private final String id;
    private final String address;
    private final int port;

    private ClusterNodeNotificationMessage(ClusterNodeNotificationType type, String id, String address, int port) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.CLUSTER_NODE_NOTIFICATION;
    }

    public ClusterNodeNotificationType type() {
        return type;
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
                .add("type", type)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }

    public static class Builder {

        private String id;
        private String address;
        private int port;

        private final ClusterNodeNotificationType type;

        private Builder(ClusterNodeNotificationType type) {
            this.type = type;
        }

        public ClusterNodeNotificationMessage.Builder id(String id) {
            this.id = id;
            return this;
        }

        public ClusterNodeNotificationMessage.Builder address(String address) {
            this.address = address;
            return this;
        }

        public ClusterNodeNotificationMessage.Builder port(int port) {
            this.port = port;
            return this;
        }

        public ClusterNodeNotificationMessage build() {
            return new ClusterNodeNotificationMessage(type, id, address, port);
        }
    }
}
