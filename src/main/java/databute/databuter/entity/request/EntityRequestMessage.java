package databute.databuter.entity.request;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityType;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityRequestMessage implements Message {

    private final EntityRequestType requestType;
    private final String key;
    private final EntityType valueType;
    private final Object value;

    public EntityRequestMessage(EntityRequestType requestType, String key, EntityType valueType, Object value) {
        this.requestType = checkNotNull(requestType, "requestType");
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTITY_REQUEST;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("requestType", requestType)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
