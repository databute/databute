package databute.databuter.cluster.handshake.response;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeResponseMessageHandler extends ClusterMessageHandler<HandshakeResponseMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HandshakeResponseMessageHandler.class);

    public HandshakeResponseMessageHandler(ClusterSession session) {
        super(session);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeResponseMessage handshakeResponse) {
        logger.debug("Read handshake response: {}", handshakeResponse);
    }
}
