package databute.databuter.bucket.notification.update;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketUpdatedNotificationMessageSerializer implements MessageSerializer<BucketUpdatedNotificationMessage> {

    @Override
    public Packet serialize(BucketUpdatedNotificationMessage bucketUpdatedNotification) {
        checkNotNull(bucketUpdatedNotification, "bucketUpdatedNotification");

        final Packet packet = new BufferedPacket();
        packet.writeString(bucketUpdatedNotification.id());
        packet.writeString(bucketUpdatedNotification.activeNodeId());
        packet.writeString(bucketUpdatedNotification.standbyNodeId());
        return packet;
    }
}
