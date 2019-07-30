package databute.databuter.entry.result.fail;

import databute.databuter.Databuter;
import databute.databuter.entry.*;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryOperationFailMessageHandler extends AbstractMessageHandler<Session, EntryOperationFailMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntryOperationFailMessageHandler.class);

    public EntryOperationFailMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntryOperationFailMessage entryOperationFailMessage) {
        logger.debug("Handling entry operation fail message {}", entryOperationFailMessage);

        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        final String id = entryOperationFailMessage.id();
        final EntryCallback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            switch (entryOperationFailMessage.errorCode()) {
                case NOT_FOUND:
                    callback.onFailure(new NotFoundException(entryOperationFailMessage.key()));
                    break;
                case EMPTY_KEY:
                    callback.onFailure(new EmptyEntryKeyException(entryOperationFailMessage.key()));
                    break;
                case DUPLICATE_KEY:
                    callback.onFailure(new DuplicateEntryKeyException(entryOperationFailMessage.key()));
                    break;
                case UNSUPPORTED_VALUE_TYPE:
                    callback.onFailure(new UnsupportedValueTypeException());
                    break;
            }
        }
    }
}
