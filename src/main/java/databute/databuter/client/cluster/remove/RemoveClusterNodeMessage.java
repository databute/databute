package databute.databuter.client.cluster.remove;

import com.google.common.base.MoreObjects;
import databute.databuter.client.network.ClientMessageCode;
import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;

public class RemoveClusterNodeMessage implements Message {

    private final String id;

    public RemoveClusterNodeMessage(String id) {
        this.id = id;
    }

    @Override
    public MessageCode messageCode() {
        return ClientMessageCode.REMOVE_CLUSTER_NODE;
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .toString();
    }
}
