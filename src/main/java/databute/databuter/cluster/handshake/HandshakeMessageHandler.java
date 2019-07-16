package databute.databuter.cluster.handshake;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeMessageHandler extends SimpleChannelInboundHandler<HandshakeMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HandshakeMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeMessage handshake) throws Exception {
        logger.debug("Read handshake {}", handshake);
    }
}
