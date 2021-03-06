package databute.databutee.entry.result.fail;

import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.Dispatcher;
import databute.databutee.entry.DuplicateEntryKeyException;
import databute.databutee.entry.EmptyEntryKeyException;
import databute.databutee.entry.NotFoundException;
import databute.databutee.entry.UnsupportedValueTypeException;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryOperationFailMessageHandler extends MessageHandler<EntryOperationFailMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntryOperationFailMessageHandler.class);

    public EntryOperationFailMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(EntryOperationFailMessage entryOperationFailMessage) {
        final Databutee databutee = session().databutee();
        final Dispatcher dispatcher = databutee.dispatcher();

        final String id = entryOperationFailMessage.id();
        final Callback callback = dispatcher.dequeue(id);
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
