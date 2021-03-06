package databute.databuter.cluster.network;

import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessageHandler;
import databute.databuter.entry.delete.DeleteEntryMessageHandler;
import databute.databuter.entry.expire.ExpireEntryMessageHandler;
import databute.databuter.entry.get.GetEntryMessageHandler;
import databute.databuter.entry.set.SetEntryMessageHandler;
import databute.databuter.entry.update.UpdateEntryMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class InboundClusterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(InboundClusterChannelHandler.class);

    private ClusterSession session;

    private final ClusterCoordinator clusterCoordinator;

    public InboundClusterChannelHandler(ClusterCoordinator clusterCoordinator) {
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new ClusterSession(channel, clusterCoordinator);
        logger.info("Active new cluster inbound session {}", session);

        configurePipeline(ctx);
    }

    private void configurePipeline(ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast(new HandshakeRequestMessageHandler(session));
        pipeline.addLast(new GetEntryMessageHandler(session));
        pipeline.addLast(new SetEntryMessageHandler(session));
        pipeline.addLast(new UpdateEntryMessageHandler(session));
        pipeline.addLast(new DeleteEntryMessageHandler(session));
        pipeline.addLast(new ExpireEntryMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster inbound session {}", session);
    }
}
