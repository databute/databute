package databute.databuter.cluster.handshake.response;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandshakeResponseMessageHandler extends ClusterMessageHandler<HandshakeResponseMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HandshakeResponseMessageHandler.class);

    public HandshakeResponseMessageHandler(ClusterSession session) {
        super(session);
    }

    @Override
    public void handle(HandshakeResponseMessage handshakeResponse) {
        logger.debug("Read handshake response: {}", handshakeResponse);
    }
}
