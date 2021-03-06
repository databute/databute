package databute.databutee.network.message;

import databute.databutee.network.DatabuterSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class MessageHandler<M extends Message> extends SimpleChannelInboundHandler<M> {

    private final DatabuterSession session;

    protected MessageHandler(DatabuterSession session) {
        this.session = checkNotNull(session, "session");
    }

    public abstract void handle(M message);

    public final DatabuterSession session() {
        return session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, M message) {
        handle(message);
    }
}
