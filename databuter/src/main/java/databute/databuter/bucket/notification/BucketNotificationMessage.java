package databute.databuter.bucket.notification;

import com.google.common.base.MoreObjects;
import databute.network.message.Message;
import databute.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketNotificationMessage implements Message {

    public static BucketNotificationMessage.Builder added() {
        return new BucketNotificationMessage.Builder(BucketNotificationType.ADDED);
    }

    public static BucketNotificationMessage.Builder updated() {
        return new BucketNotificationMessage.Builder(BucketNotificationType.UPDATED);
    }

    public static BucketNotificationMessage.Builder removed() {
        return new BucketNotificationMessage.Builder(BucketNotificationType.REMOVED);
    }

    private final BucketNotificationType type;
    private final String id;
    private final int keyFactor;
    private final String activeNodeId;
    private final String standbyNodeId;

    public BucketNotificationMessage(BucketNotificationType type,
                                     String id,
                                     int keyFactor,
                                     String activeNodeId,
                                     String standbyNodeId) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.keyFactor = keyFactor;
        this.activeNodeId = activeNodeId;
        this.standbyNodeId = standbyNodeId;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.BUCKET_NOTIFICATION;
    }

    public BucketNotificationType type() {
        return type;
    }

    public String id() {
        return id;
    }

    public int keyFactor() {
        return keyFactor;
    }

    public String activeNodeId() {
        return activeNodeId;
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("type", type)
                .add("id", id)
                .add("keyFactor", keyFactor)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }

    public static class Builder {

        private String id;
        private int keyFactor;
        private String activeNodeId;
        private String standbyNodeId;

        private final BucketNotificationType type;

        private Builder(BucketNotificationType type) {
            this.type = type;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder keyFactor(int keyFactor) {
            this.keyFactor = keyFactor;
            return this;
        }

        public Builder activeNodeId(String activeNodeId) {
            this.activeNodeId = activeNodeId;
            return this;
        }

        public Builder standbyNodeId(String standbyNodeId) {
            this.standbyNodeId = standbyNodeId;
            return this;
        }

        public BucketNotificationMessage build() {
            return new BucketNotificationMessage(type, id, keyFactor, activeNodeId, standbyNodeId);
        }
    }
}
