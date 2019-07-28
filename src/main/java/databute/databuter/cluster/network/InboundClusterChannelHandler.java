package databute.databuter.cluster.network;

import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessageHandler;
import databute.databuter.entity.delete.DeleteEntityMessageHandler;
import databute.databuter.entity.get.GetEntityMessageHandler;
import databute.databuter.entity.set.SetEntityMessageHandler;
import databute.databuter.entity.update.UpdateEntityMessageHandler;
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
        pipeline.addLast(new GetEntityMessageHandler(session));
        pipeline.addLast(new SetEntityMessageHandler(session));
        pipeline.addLast(new UpdateEntityMessageHandler(session));
        pipeline.addLast(new DeleteEntityMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster inbound session {}", session);
    }
}
