package databute.databuter.entity.request;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityRequestMessageHandler extends ClientMessageHandler<EntityRequestMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityRequestMessageHandler.class);

    public EntityRequestMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(EntityRequestMessage entityRequestMessage) {
        logger.debug("Handling entity request message {}", entityRequestMessage);
    }
}
