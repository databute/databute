package databute.databuter.cluster.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundClusterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(InboundClusterChannelHandler.class);

    private ClusterSession session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new ClusterSession(channel);
        logger.info("Active new cluster inbound session {}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster inbound session {}", session);
    }
}
