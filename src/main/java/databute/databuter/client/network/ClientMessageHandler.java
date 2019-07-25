package databute.databuter.client.network;

import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ClientMessageHandler<M extends Message> extends SimpleChannelInboundHandler<M>
        implements MessageHandler<ClientSession, M> {

    private final ClientSession session;

    protected ClientMessageHandler(ClientSession session) {
        this.session = checkNotNull(session, "session");
    }

    @Override
    public ClientSession session() {
        return session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, M message) {
        handle(message);
    }
}
