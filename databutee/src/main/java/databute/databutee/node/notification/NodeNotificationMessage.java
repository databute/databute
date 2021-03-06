package databute.databutee.node.notification;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class NodeNotificationMessage implements Message {

    private final NodeNotificationType type;
    private final String id;
    private final String address;
    private final int port;

    public NodeNotificationMessage(NodeNotificationType type, String id, String address, int port) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.NODE_NOTIFICATION;
    }

    public NodeNotificationType type() {
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
}
