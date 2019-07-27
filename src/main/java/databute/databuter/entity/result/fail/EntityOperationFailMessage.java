package databute.databuter.entity.result.fail;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityMessage;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationFailMessage implements EntityMessage {

    public static EntityOperationFailMessage notFound(String id, String key) {
        return new EntityOperationFailMessage(id, key, EntityOperationErrorCode.NOT_FOUND);
    }

    public static EntityOperationFailMessage emptyKey(String id, String key) {
        return new EntityOperationFailMessage(id, key, EntityOperationErrorCode.EMPTY_KEY);
    }

    public static EntityOperationFailMessage duplicateKey(String id, String key) {
        return new EntityOperationFailMessage(id, key, EntityOperationErrorCode.DUPLICATE_KEY);
    }

    public static EntityOperationFailMessage unsupportedValueType(String id, String key) {
        return new EntityOperationFailMessage(id, key, EntityOperationErrorCode.UNSUPPORTED_VALUE_TYPE);
    }

    private final String id;
    private final String key;
    private final EntityOperationErrorCode errorCode;

    public EntityOperationFailMessage(String id, String key, EntityOperationErrorCode errorCode) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.errorCode = checkNotNull(errorCode, "errorCode");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTITY_OPERATION_FAIL;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public EntityOperationErrorCode errorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("errorCode", errorCode)
                .toString();
    }
}
