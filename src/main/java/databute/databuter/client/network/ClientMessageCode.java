package databute.databuter.client.network;

import databute.databuter.network.message.MessageCode;

public enum ClientMessageCode implements MessageCode {

    REGISTER(0),
    ADD_CLUSTER_NODE(1),
    REMOVE_CLUSTER_NODE(2),
    BUCKET_NOTIFICATION(3);

    private final int value;

    ClientMessageCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
