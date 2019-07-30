package databute.databuter.client.network;

import databute.databuter.Databuter;
import databute.databuter.client.register.RegisterMessageHandler;
import databute.databuter.entry.delete.DeleteEntryMessageHandler;
import databute.databuter.entry.expire.ExpireEntryMessageHandler;
import databute.databuter.entry.get.GetEntryMessageHandler;
import databute.databuter.entry.set.SetEntryMessageHandler;
import databute.databuter.entry.update.UpdateEntryMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);

    private ClientSession session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new ClientSession(channel);
        logger.info("Active new client session {}", session);

        Databuter.instance().clientSessionGroup().add(session);

        configurePipeline(ctx);
    }

    private void configurePipeline(ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast(new RegisterMessageHandler(session));
        pipeline.addLast(new GetEntryMessageHandler(session));
        pipeline.addLast(new SetEntryMessageHandler(session));
        pipeline.addLast(new UpdateEntryMessageHandler(session));
        pipeline.addLast(new DeleteEntryMessageHandler(session));
        pipeline.addLast(new ExpireEntryMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        removeFromSessionGroup();

        logger.info("Inactive client session {}", session);
    }

    private void removeFromSessionGroup() {
        final ChannelId channelId = session.channel().id();

        removeSessionFromSessionGroup(channelId);
        removeListeningSessionFromSessionGroup(channelId);
    }

    private void removeSessionFromSessionGroup(ChannelId channelId) {
        final boolean removed = (Databuter.instance().clientSessionGroup().removeSession(channelId) != null);
        if (removed) {
            logger.debug("Removed client session {}", session.channel().id());
        }
    }

    private void removeListeningSessionFromSessionGroup(ChannelId channelId) {
        final boolean removed = (Databuter.instance().clientSessionGroup().removeListeningSession(channelId) != null);
        if (removed) {
            logger.debug("Removed listening client session {}", channelId);
        }
    }
}
