package databute.databuter.cluster.network;

import databute.databuter.network.message.Message;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ClusterMessageHandler<M extends Message> extends SimpleChannelInboundHandler<M> {

    private final ClusterSession session;

    protected ClusterMessageHandler(ClusterSession session) {
        this.session = checkNotNull(session, "session");
    }

    protected final ClusterSession session() {
        return session;
    }
}
