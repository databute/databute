package databute.databuter.network.message;

public enum MessageCode {

    REGISTER(0),
    CLUSTER_NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2),
    HANDSHAKE_REQUEST(100),
    HANDSHAKE_RESPONSE(101);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
