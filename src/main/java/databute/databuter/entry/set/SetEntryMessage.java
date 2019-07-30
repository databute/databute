package databute.databuter.entry.set;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.EntityMessage;
import databute.databuter.entry.EntityValueType;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessage implements EntityMessage {

    private final String id;
    private final String key;
    private final EntityValueType valueType;
    private final Object value;

    public SetEntityMessage(String id, String key, EntityValueType valueType, Object value) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.SET_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public EntityValueType valueType() {
        return valueType;
    }

    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
