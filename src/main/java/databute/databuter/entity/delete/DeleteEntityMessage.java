package databute.databuter.entity.delete;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityMessage;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntityMessage implements EntityMessage {

    private final String id;
    private final String key;

    public DeleteEntityMessage(String id, String key) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.DELETE_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    public String key() {
        return key;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .toString();
    }
}
