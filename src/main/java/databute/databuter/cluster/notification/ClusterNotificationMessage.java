package databute.databuter.cluster.notification;

import com.google.common.base.MoreObjects;
import databute.databuter.client.network.ClientMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNotificationMessage implements Message {

    public static ClusterNotificationMessage.Builder added() {
        return new ClusterNotificationMessage.Builder(ClusterNotificationType.ADDED);
    }

    public static ClusterNotificationMessage.Builder removed() {
        return new ClusterNotificationMessage.Builder(ClusterNotificationType.REMOVED);
    }

    private final ClusterNotificationType type;
    private final String id;
    private final String address;
    private final int port;

    private ClusterNotificationMessage(ClusterNotificationType type, String id, String address, int port) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    @Override
    public MessageCode messageCode() {
        return ClientMessageCode.CLUSTER_NOTIFICATION;
    }

    public ClusterNotificationType type() {
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

        private final ClusterNotificationType type;

        private Builder(ClusterNotificationType type) {
            this.type = type;
        }

        public ClusterNotificationMessage.Builder id(String id) {
            this.id = id;
            return this;
        }

        public ClusterNotificationMessage.Builder address(String address) {
            this.address = address;
            return this;
        }

        public ClusterNotificationMessage.Builder port(int port) {
            this.port = port;
            return this;
        }

        public ClusterNotificationMessage build() {
            return new ClusterNotificationMessage(type, id, address, port);
        }
    }
}
