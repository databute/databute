package databute.databuter.cluster.network;

import databute.databuter.network.message.AbstractMessageHandler;
import databute.databuter.network.message.Message;

public abstract class ClusterMessageHandler<M extends Message> extends AbstractMessageHandler<ClusterSession, M> {

    protected ClusterMessageHandler(ClusterSession session) {
        super(session);
    }
}
