package databute.databuter.entity.delete;

import com.google.common.base.MoreObjects;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntityMessage implements Message {

    private final String key;

    public DeleteEntityMessage(String key) {
        this.key = checkNotNull(key, "key");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.DELETE_ENTITY;
    }

    public String key() {
        return key;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("key", key)
                .toString();
    }
}
