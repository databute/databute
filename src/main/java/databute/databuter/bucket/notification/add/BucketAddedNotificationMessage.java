package databute.databuter.bucket.notification.add;

import com.google.common.base.MoreObjects;
import databute.databuter.client.network.ClientMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketAddedNotificationMessage implements Message {

    public static BucketAddedNotificationMessage.Builder builder() {
        return new BucketAddedNotificationMessage.Builder();
    }

    private final String id;
    private final String activeNodeId;
    private final String standbyNodeId;

    public BucketAddedNotificationMessage(String id, String activeNodeId, String standbyNodeId) {
        this.id = checkNotNull(id, "id");
        this.activeNodeId = activeNodeId;
        this.standbyNodeId = standbyNodeId;
    }

    @Override
    public MessageCode messageCode() {
        return ClientMessageCode.BUCKET_ADDED_NOTIFICATION;
    }

    public String id() {
        return id;
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
                .add("id", id)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }

    public static class Builder {

        private String id;
        private String activeNodeId;
        private String standbyNodeId;

        private Builder() {
            super();
        }

        public Builder id(String id) {
            this.id = id;
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

        public BucketAddedNotificationMessage build() {
            return new BucketAddedNotificationMessage(id, activeNodeId, standbyNodeId);
        }
    }
}
