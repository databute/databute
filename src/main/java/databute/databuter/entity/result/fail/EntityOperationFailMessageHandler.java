package databute.databuter.entity.result.fail;

import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityOperationFailMessageHandler extends AbstractMessageHandler<Session, EntityOperationFailMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationFailMessageHandler.class);

    public EntityOperationFailMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationFailMessage entityOperationFailMessage) {
        logger.debug("Handling entity operation fail message {}", entityOperationFailMessage);
    }
}
