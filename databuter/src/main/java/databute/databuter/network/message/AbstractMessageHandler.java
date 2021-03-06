package databute.databuter.network.message;

import databute.databuter.network.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractMessageHandler<S extends Session, M extends Message>
        extends SimpleChannelInboundHandler<M>
        implements MessageHandler<S, M> {

    private final S session;

    protected AbstractMessageHandler(S session) {
        this.session = checkNotNull(session, "session");
    }

    @Override
    public final S session() {
        return session;
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, M message) {
        handle(message);
    }
}
