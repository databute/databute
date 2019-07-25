package databute.databuter.bucket.notification.add;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketAddedNotificationMessageSerializer implements MessageSerializer<BucketAddedNotificationMessage> {

    @Override
    public Packet serialize(BucketAddedNotificationMessage bucketAddedNotification) {
        checkNotNull(bucketAddedNotification, "bucketAddedNotification");

        final Packet packet = new BufferedPacket();
        packet.writeString(bucketAddedNotification.id());
        packet.writeString(bucketAddedNotification.activeNodeId());
        packet.writeString(bucketAddedNotification.standbyNodeId());
        return packet;
    }
}
