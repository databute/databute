package databute.databuter.cluster.notification;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNotificationMessageSerializer implements MessageSerializer<ClusterNotificationMessage> {

    @Override
    public Packet serialize(ClusterNotificationMessage clusterNotificationMessage) {
        checkNotNull(clusterNotificationMessage, "clusterNotificationMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(clusterNotificationMessage.type().name());
        packet.writeString(clusterNotificationMessage.id());
        packet.writeString(clusterNotificationMessage.address());
        packet.writeInt(clusterNotificationMessage.port());
        return packet;
    }
}
