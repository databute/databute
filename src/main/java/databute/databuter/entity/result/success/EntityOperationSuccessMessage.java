package databute.databuter.entity.result.success;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.*;
import databute.databuter.entity.type.IntegerEntity;
import databute.databuter.entity.type.LongEntity;
import databute.databuter.entity.type.StringEntity;
import databute.databuter.network.message.MessageCode;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationSuccessMessage implements EntityMessage {

    public static EntityOperationSuccessMessage entity(String id, Entity entity) {
        checkNotNull(entity, "entity");

        if (entity instanceof IntegerEntity) {
            final IntegerEntity integerEntity = (IntegerEntity) entity;
            return new EntityOperationSuccessMessage(id, integerEntity.key(), EntityValueType.INTEGER,
                    integerEntity.value(), integerEntity.createdTimestamp(), integerEntity.lastUpdatedTimestamp());
        } else if (entity instanceof LongEntity) {
            final LongEntity longEntity = (LongEntity) entity;
            return new EntityOperationSuccessMessage(id, longEntity.key(), EntityValueType.LONG, longEntity.value(),
                    longEntity.createdTimestamp(), longEntity.lastUpdatedTimestamp());
        } else if (entity instanceof StringEntity) {
            final StringEntity stringEntity = (StringEntity) entity;
            return new EntityOperationSuccessMessage(id, stringEntity.key(), EntityValueType.STRING,
                    stringEntity.value(), stringEntity.createdTimestamp(), stringEntity.lastUpdatedTimestamp());
        } else {
            throw new UnsupportedValueTypeException();
        }
    }

    private final String id;
    private final EntityKey key;
    private final EntityValueType valueType;
    private final Object value;
    private final Instant createdTimestamp;
    private final Instant lastUpdatedTimestamp;

    public EntityOperationSuccessMessage(String id,
                                         EntityKey key,
                                         EntityValueType valueType,
                                         Object value,
                                         Instant createdTimestamp,
                                         Instant lastUpdatedTimestamp) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTITY_OPERATION_SUCCESS;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key.key();
    }

    public EntityValueType valueType() {
        return valueType;
    }

    public Object value() {
        return value;
    }

    public Instant createdTimestamp() {
        return createdTimestamp;
    }

    public Instant lastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .add("createdTimestamp", createdTimestamp)
                .add("lastUpdatedTimestamp", lastUpdatedTimestamp)
                .toString();
    }
}
