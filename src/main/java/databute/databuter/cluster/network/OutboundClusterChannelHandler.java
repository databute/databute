package databute.databuter.cluster.network;

import databute.databuter.cluster.ClusterNode;
import databute.databuter.cluster.handshake.HandshakeRequestMessage;
import databute.databuter.cluster.handshake.HandshakeRequestMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class OutboundClusterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OutboundClusterChannelHandler.class);

    private ClusterSession session;

    private final ClusterNode node;

    public OutboundClusterChannelHandler(ClusterNode node) {
        this.node = checkNotNull(node, "node");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new ClusterSession(channel);
        session.node(node);
        logger.info("Active new cluster outbound session {}", session);

        configurePipeline(ctx);

        channel.writeAndFlush(new HandshakeRequestMessage("HELLO WORLD"));
    }

    private void configurePipeline(ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast(new HandshakeRequestMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster outbound session {}", session);
    }
}
