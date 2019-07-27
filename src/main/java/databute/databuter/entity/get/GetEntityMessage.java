package databute.databuter.entity.get;

import com.google.common.base.MoreObjects;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessage implements Message {

    private final String key;

    public GetEntityMessage(String key) {
        this.key = checkNotNull(key, "key");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.GET_ENTITY;
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
