package databute.databuter.entry.result.fail;

import com.google.common.base.MoreObjects;
import databute.databuter.entry.EntryMessage;
import databute.databuter.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryOperationFailMessage implements EntryMessage {

    public static EntryOperationFailMessage notFound(String id, String key) {
        return new EntryOperationFailMessage(id, key, EntryOperationErrorCode.NOT_FOUND);
    }

    public static EntryOperationFailMessage emptyKey(String id, String key) {
        return new EntryOperationFailMessage(id, key, EntryOperationErrorCode.EMPTY_KEY);
    }

    public static EntryOperationFailMessage duplicateKey(String id, String key) {
        return new EntryOperationFailMessage(id, key, EntryOperationErrorCode.DUPLICATE_KEY);
    }

    public static EntryOperationFailMessage unsupportedValueType(String id, String key) {
        return new EntryOperationFailMessage(id, key, EntryOperationErrorCode.UNSUPPORTED_VALUE_TYPE);
    }

    private final String id;
    private final String key;
    private final EntryOperationErrorCode errorCode;

    public EntryOperationFailMessage(String id, String key, EntryOperationErrorCode errorCode) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.errorCode = checkNotNull(errorCode, "errorCode");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTRY_OPERATION_FAIL;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public EntryOperationErrorCode errorCode() {
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
