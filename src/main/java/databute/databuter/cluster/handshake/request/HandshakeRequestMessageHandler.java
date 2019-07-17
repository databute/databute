package databute.databuter.cluster.handshake.request;

import databute.databuter.cluster.handshake.response.HandshakeResponseMessage;
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
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeRequestMessage handshakeRequest) {
        logger.debug("Read handshake request {}", handshakeRequest);

        if (StringUtils.equals(session().cluster().id(), handshakeRequest.id())) {
            session().send(new HandshakeResponseMessage());
        } else {
            logger.warn("Detected invalid handshake request from {} to {}.", handshakeRequest.id(), session().cluster().id());
            ctx.close().addListener(future -> logger.warn("Closed invalid session {}", session()));
        }
    }
}
