package databute.databuter.cluster.handshake;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
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

        if (StringUtils.equals(session().cluster().id(), handshake.id())) {
            ctx.writeAndFlush(new HandshakeResponseMessage());
        } else {
            logger.warn("Detected invalid handshake request from {} to {}.", handshake.id(), session().cluster().id());
            ctx.close().addListener(future -> logger.warn("Closed invalid session {}", session()));
        }
    }
}
