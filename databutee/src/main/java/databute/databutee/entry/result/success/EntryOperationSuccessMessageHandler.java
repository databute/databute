package databute.databutee.entry.result.success;

import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.Dispatcher;
import databute.databutee.entry.EmptyEntryKeyException;
import databute.databutee.entry.Entry;
import databute.databutee.entry.EntryKey;
import databute.databutee.network.DatabuterMessageHandler;
import databute.databutee.network.DatabuterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class EntryOperationSuccessMessageHandler extends DatabuterMessageHandler<EntryOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntryOperationSuccessMessageHandler.class);

    public EntryOperationSuccessMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(EntryOperationSuccessMessage entryOperationSuccessMessage) {
        final Databutee databutee = session().databutee();
        final Dispatcher dispatcher = databutee.dispatcher();

        final String id = entryOperationSuccessMessage.id();
        final Callback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            try {
                final EntryKey entryKey = new EntryKey(entryOperationSuccessMessage.key());
                final Object value = entryOperationSuccessMessage.value();
                final Instant createdTimestamp = entryOperationSuccessMessage.createdTimestamp();
                final Instant lastUpdatedTimestamp = entryOperationSuccessMessage.lastUpdatedTimestamp();
                final Entry entry = new Entry(entryKey, value, createdTimestamp, lastUpdatedTimestamp);
                callback.onSuccess(entry);
            } catch (EmptyEntryKeyException e) {
                callback.onFailure(e);
            }
        }
    }
}
