package databute.databuter.entry.get;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entry.*;
import databute.databuter.entry.result.fail.EntryOperationFailMessage;
import databute.databuter.entry.result.success.EntryOperationSuccessMessage;
import databute.network.Session;
import databute.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEntryMessageHandler extends AbstractMessageHandler<Session, GetEntryMessage> {

    private static final Logger logger = LoggerFactory.getLogger(GetEntryMessageHandler.class);

    public GetEntryMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(GetEntryMessage getEntryMessage) {
        logger.debug("Handling get entry message {}", getEntryMessage);

        final String id = getEntryMessage.id();
        final String key = getEntryMessage.key();

        try {
            final EntryKey entryKey = new EntryKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entryKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.get(entryKey, new EntryCallback() {
                    @Override
                    public void onSuccess(Entry entry) {
                        session().send(EntryOperationSuccessMessage.entry(id, entry));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof NotFoundException) {
                            session().send(EntryOperationFailMessage.notFound(id, key));
                        } else if (e instanceof EmptyEntryKeyException) {
                            session().send(EntryOperationFailMessage.emptyKey(id, key));
                        } else if (e instanceof DuplicateEntryKeyException) {
                            session().send(EntryOperationFailMessage.duplicateKey(id, key));
                        } else if (e instanceof UnsupportedValueTypeException) {
                            session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
                        } else {
                            logger.error("Unknown error to get entry {}", key, e);
                        }
                    }
                });
            }
        } catch (EmptyEntryKeyException e) {
            session().send(EntryOperationFailMessage.emptyKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntryOperationFailMessage.unsupportedValueType(id, key));
        }
    }
}
