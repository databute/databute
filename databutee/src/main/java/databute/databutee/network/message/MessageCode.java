package databute.databutee.network.message;

public enum MessageCode {

    REGISTER(0),
    NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2),
    GET_ENTRY(3),
    SET_ENTRY(4),
    UPDATE_ENTRY(5),
    DELETE_ENTRY(6),
    ENTRY_OPERATION_SUCCESS(7),
    ENTRY_OPERATION_FAIL(8),
    EXPIRE_ENTRY(9);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
