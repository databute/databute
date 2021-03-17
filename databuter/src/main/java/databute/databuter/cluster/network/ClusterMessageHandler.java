package databute.databuter.cluster.network;

import databute.network.message.AbstractMessageHandler;
import databute.network.message.Message;

public abstract class ClusterMessageHandler<M extends Message> extends AbstractMessageHandler<ClusterSession, M> {

    protected ClusterMessageHandler(ClusterSession session) {
        super(session);
    }
}
