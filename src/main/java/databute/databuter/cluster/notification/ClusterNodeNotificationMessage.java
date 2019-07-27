package databute.databuter.cluster.notification;

import com.google.common.base.MoreObjects;
import databute.databuter.network.Endpoint;
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
    private final Endpoint endpoint;

    private ClusterNodeNotificationMessage(ClusterNodeNotificationType type,
                                           String id,
                                           Endpoint endpoint) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.endpoint = checkNotNull(endpoint, "endpoint");
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

    public Endpoint endpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("type", type)
                .add("id", id)
                .add("endpoint", endpoint)
                .toString();
    }

    public static class Builder {

        private String id;
        private Endpoint endpoint;

        private final ClusterNodeNotificationType type;

        private Builder(ClusterNodeNotificationType type) {
            this.type = type;
        }

        public ClusterNodeNotificationMessage.Builder id(String id) {
            this.id = id;
            return this;
        }

        public ClusterNodeNotificationMessage.Builder endpoint(Endpoint endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public ClusterNodeNotificationMessage build() {
            return new ClusterNodeNotificationMessage(type, id, endpoint);
        }
    }
}
