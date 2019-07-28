package databute.databuter.entity.result.success;

import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityOperationSuccessMessageHandler extends AbstractMessageHandler<Session, EntityOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationSuccessMessageHandler.class);

    public EntityOperationSuccessMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationSuccessMessage entityOperationSuccessMessage) {
        logger.debug("Handling entity operation success message {}", entityOperationSuccessMessage);
    }
}
