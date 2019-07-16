package databute.databuter.cluster.handshake;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeRequestMessageHandler extends ClusterMessageHandler<HandshakeRequestMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HandshakeRequestMessageHandler.class);

    public HandshakeRequestMessageHandler(ClusterSession session) {
        super(session);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeRequestMessage handshake) throws Exception {
        logger.debug("Read handshake {}", handshake);
    }
}
