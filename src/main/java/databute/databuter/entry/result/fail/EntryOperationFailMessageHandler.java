package databute.databuter.entry.result.fail;

import databute.databuter.Databuter;
import databute.databuter.entry.*;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityOperationFailMessageHandler extends AbstractMessageHandler<Session, EntryOperationFailMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationFailMessageHandler.class);

    public EntityOperationFailMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntryOperationFailMessage entryOperationFailMessage) {
        logger.debug("Handling entry operation fail message {}", entryOperationFailMessage);

        final EntityMessageDispatcher dispatcher = Databuter.instance().entityMessageDispatcher();

        final String id = entryOperationFailMessage.id();
        final EntityCallback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            switch (entryOperationFailMessage.errorCode()) {
                case NOT_FOUND:
                    callback.onFailure(new NotFoundException(entryOperationFailMessage.key()));
                    break;
                case EMPTY_KEY:
                    callback.onFailure(new EmptyEntityKeyException(entryOperationFailMessage.key()));
                    break;
                case DUPLICATE_KEY:
                    callback.onFailure(new DuplicateEntityKeyException(entryOperationFailMessage.key()));
                    break;
                case UNSUPPORTED_VALUE_TYPE:
                    callback.onFailure(new UnsupportedValueTypeException());
                    break;
            }
        }
    }
}
