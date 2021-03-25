package databute.databuter.entry.result.success;

import databute.databuter.Databuter;
import databute.databuter.entry.*;
import databute.databuter.entry.type.*;
import databute.network.Session;
import databute.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                case LIST:
                    callListEntry(callback, entryOperationSuccessMessage);
                    break;
                case SET:
                    callSetEntry(callback, entryOperationSuccessMessage);
                    break;
                case DICTIONARY:
                    callDictionaryEntry(callback, entryOperationSuccessMessage);
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

    @SuppressWarnings("unchecked")
    private void callListEntry(EntryCallback callback,
                               EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final List<String> value = (List<String>) entryOperationSuccessMessage.value();
        final Instant createdTimestamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new ListEntry(entryKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }

    @SuppressWarnings("unchecked")
    private void callSetEntry(EntryCallback callback,
                              EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final Set<String> value = (Set<String>) entryOperationSuccessMessage.value();
        final Instant createdTimeStamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new SetEntry(entryKey, value, createdTimeStamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }

    @SuppressWarnings("unchecked")
    private void callDictionaryEntry(EntryCallback callback,
                                     EntryOperationSuccessMessage entryOperationSuccessMessage)
            throws EmptyEntryKeyException {
        final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
        final Map<String, String> value = (Map<String, String>) entryOperationSuccessMessage.value();
        final Instant createdTimeStamp = entryOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entryOperationSuccessMessage.expirationTimestamp();

        final Entry entry = new DictionaryEntry(entryKey, value, createdTimeStamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entry);
    }
}
