package databute.databuter.entity.result.success;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityMessage;
import databute.databuter.entity.EntityValueType;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.entity.type.*;
import databute.databuter.network.message.MessageCode;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationSuccessMessage implements EntityMessage {

    public static EntityOperationSuccessMessage entity(String id, Entity entity) {
        checkNotNull(entity, "entity");

        if (entity instanceof IntegerEntity) {
            final IntegerEntity integerEntity = (IntegerEntity) entity;
            return new EntityOperationSuccessMessage(id, integerEntity.key().key(),
                    EntityValueType.INTEGER, integerEntity.value(),
                    integerEntity.createdTimestamp(), integerEntity.lastUpdatedTimestamp(),
                    integerEntity.expirationTimestamp());
        } else if (entity instanceof LongEntity) {
            final LongEntity longEntity = (LongEntity) entity;
            return new EntityOperationSuccessMessage(id, longEntity.key().key(),
                    EntityValueType.LONG, longEntity.value(),
                    longEntity.createdTimestamp(), longEntity.lastUpdatedTimestamp(),
                    longEntity.expirationTimestamp());
        } else if (entity instanceof StringEntity) {
            final StringEntity stringEntity = (StringEntity) entity;
            return new EntityOperationSuccessMessage(id, stringEntity.key().key(),
                    EntityValueType.STRING, stringEntity.value(),
                    stringEntity.createdTimestamp(), stringEntity.lastUpdatedTimestamp(),
                    stringEntity.expirationTimestamp());
        } else if (entity instanceof ListEntity) {
            final ListEntity listEntity = (ListEntity) entity;
            return new EntityOperationSuccessMessage(id, listEntity.key().key(),
                    EntityValueType.LIST, listEntity.value(),
                    listEntity.createdTimestamp(), listEntity.lastUpdatedTimestamp(),
                    listEntity.expirationTimestamp());
        } else if (entity instanceof SetEntity) {
            final SetEntity setEntity = (SetEntity) entity;
            return new EntityOperationSuccessMessage(id, setEntity.key().key(),
                    EntityValueType.SET, setEntity.value(),
                    setEntity.createdTimestamp(), setEntity.lastUpdatedTimestamp(),
                    setEntity.expirationTimestamp());
        } else if (entity instanceof DictionaryEntity) {
            final DictionaryEntity dictionaryEntity = (DictionaryEntity) entity;
            return new EntityOperationSuccessMessage(id, dictionaryEntity.key().key(),
                    EntityValueType.DICTIONARY, dictionaryEntity.value(),
                    dictionaryEntity.createdTimestamp(), dictionaryEntity.lastUpdatedTimestamp(),
                    dictionaryEntity.expirationTimestamp());
        } else {
            throw new UnsupportedValueTypeException();
        }
    }

    private final String id;
    private final String key;
    private final EntityValueType valueType;
    private final Object value;
    private final Instant createdTimestamp;
    private final Instant lastUpdatedTimestamp;
    private final Instant expirationTimestamp;

    public EntityOperationSuccessMessage(String id,
                                         String key,
                                         EntityValueType valueType,
                                         Object value,
                                         Instant createdTimestamp,
                                         Instant lastUpdatedTimestamp,
                                         Instant expirationTimestamp) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
        this.createdTimestamp = checkNotNull(createdTimestamp, "createdTimestamp");
        this.lastUpdatedTimestamp = checkNotNull(lastUpdatedTimestamp, "lastUpdatedTimestamp");
        this.expirationTimestamp = expirationTimestamp;
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
        return key;
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

    public Instant expirationTimestamp() {
        return expirationTimestamp;
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
                .add("expirationTimestamp", expirationTimestamp)
                .toString();
    }
}
