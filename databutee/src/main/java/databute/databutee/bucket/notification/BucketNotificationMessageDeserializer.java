package databute.databutee.bucket.notification;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketNotificationMessageDeserializer implements MessageDeserializer<BucketNotificationMessage> {

    @Override
    public BucketNotificationMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final BucketNotificationType type = BucketNotificationType.valueOf(packet.readString());
        final String id = packet.readString();
        final int keyFactor = packet.readInt();
        final String activeNodeId = packet.readString();
        final String standbyNodeId = packet.readString();
        return new BucketNotificationMessage(type, id, keyFactor, activeNodeId, standbyNodeId);
    }
}
