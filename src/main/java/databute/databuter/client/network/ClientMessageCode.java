package databute.databuter.client.network;

import databute.databuter.network.message.MessageCode;

public enum ClientMessageCode implements MessageCode {

    REGISTER(0),
    CLUSTER_NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2);

    private final int value;

    ClientMessageCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
