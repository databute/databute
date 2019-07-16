package databute.databuter.cluster.handshake;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeMessageHandler extends ClusterMessageHandler<HandshakeMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HandshakeMessageHandler.class);

    public HandshakeMessageHandler(ClusterSession session) {
        super(session);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeMessage handshake) throws Exception {
        logger.debug("Read handshake {}", handshake);
    }
}
