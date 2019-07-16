package databute.databuter.cluster.network;

import databute.databuter.network.message.MessageCode;

public enum ClusterMessageCode implements MessageCode {

    ;

    private final int value;

    ClusterMessageCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
