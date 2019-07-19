package databute.databuter.cluster.network;

import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ClusterMessageHandler<M extends Message> extends SimpleChannelInboundHandler<M>
        implements MessageHandler<ClusterSession, M> {

    private final ClusterSession session;

    protected ClusterMessageHandler(ClusterSession session) {
        this.session = checkNotNull(session, "session");
    }

    @Override
    public final ClusterSession session() {
        return session;
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, M message) {
        handle(message);
    }
}
