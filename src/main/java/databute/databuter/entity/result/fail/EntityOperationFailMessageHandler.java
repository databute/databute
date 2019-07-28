package databute.databuter.entity.result.fail;

import databute.databuter.Databuter;
import databute.databuter.entity.*;
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

        final EntityMessageDispatcher dispatcher = Databuter.instance().entityMessageDispatcher();

        final String id = entityOperationFailMessage.id();
        final EntityCallback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            switch (entityOperationFailMessage.errorCode()) {
                case NOT_FOUND:
                    callback.onFailure(new NotFoundException(entityOperationFailMessage.key()));
                    break;
                case EMPTY_KEY:
                    callback.onFailure(new EmptyEntityKeyException(entityOperationFailMessage.key()));
                    break;
                case DUPLICATE_KEY:
                    callback.onFailure(new DuplicateEntityKeyException(entityOperationFailMessage.key()));
                    break;
                case UNSUPPORTED_VALUE_TYPE:
                    callback.onFailure(new UnsupportedValueTypeException());
                    break;
            }
        }
    }
}
