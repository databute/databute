package databute.databuter.entry.result.success;

import databute.databuter.Databuter;
import databute.databuter.entry.*;
import databute.databuter.entry.type.IntegerEntry;
import databute.databuter.entry.type.LongEntry;
import databute.databuter.entry.type.StringEntry;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class EntryOperationSuccessMessageHandler extends AbstractMessageHandler<Session, EntryOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntryOperationSuccessMessageHandler.class);

    public EntryOperationSuccessMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntryOperationSuccessMessage entryOperationSuccessMessage) {
        logger.debug("Handling entry operation success message {}", entryOperationSuccessMessage);

        final EntryMessageDispatcher dispatcher = Databuter.instance().entryMessageDispatcher();

        final String id = entryOperationSuccessMessage.id();
        final EntryCallback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            call(callback, entryOperationSuccessMessage);
        }
    }

    private void call(EntryCallback callback, EntryOperationSuccessMessage entryOperationSuccessMessage) {
        try {
            switch (entryOperationSuccessMessage.valueType()) {
                case INTEGER:
                    callIntegerEntry(callback, entryOperationSuccessMessage);
                    break;
                case LONG:
                    callLongEntry(callback, entryOperationSuccessMessage);
                    break;
                case STRING:
                    callStringEntry(callback, entryOperationSuccessMessage);
                    break;
            }
        } catch (EmptyEntryKeyException e) {
            callback.onFailure(e);
        }
    }

    private void callIntegerEntry(EntryCallback callback,
                                  EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final Integer value = (Integer) entryOperationSuccessMessage.value();
        final Instant createdTimestamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new IntegerEntry(entryKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }

    private void callLongEntry(EntryCallback callback,
                               EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final Long value = (Long) entryOperationSuccessMessage.value();
        final Instant createdTimestamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new LongEntry(entryKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }

    private void callStringEntry(EntryCallback callback,
                                 EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final String value = (String) entryOperationSuccessMessage.value();
        final Instant createdTimestamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new StringEntry(entryKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }
}
