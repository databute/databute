package databute.databuter.entity.get;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityMessage;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessage implements EntityMessage {

    private final String id;
    private final String key;

    public GetEntityMessage(String id, String key) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.GET_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
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
