package databute.databuter.entity.set;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityValueType;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessage implements Message {

    private final String key;
    private final EntityValueType valueType;
    private final Object value;

    public SetEntityMessage(String key, EntityValueType valueType, Object value) {
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.SET_ENTITY;
    }

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
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
