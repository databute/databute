package databute.databuter.cluster.network;

import databute.databuter.network.message.MessageCode;

public enum ClusterMessageCode implements MessageCode {

    HANDSHAKE_REQUEST(0),
    HANDSHAKE_RESPONSE(1);

    private final int value;

    ClusterMessageCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
