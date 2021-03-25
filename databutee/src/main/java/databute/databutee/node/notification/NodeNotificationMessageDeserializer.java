package databute.databutee.node.notification;

import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class NodeNotificationMessageDeserializer implements MessageDeserializer<NodeNotificationMessage> {

    @Override
    public NodeNotificationMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final NodeNotificationType type = NodeNotificationType.valueOf(packet.readString());
        final String id = packet.readString();
        final String address = packet.readString();
        final int port = packet.readInt();
        return new NodeNotificationMessage(type, id, address, port);
    }
}
