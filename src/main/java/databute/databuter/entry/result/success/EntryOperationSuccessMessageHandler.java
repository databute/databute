package databute.databuter.entry.result.success;

import databute.databuter.Databuter;
import databute.databuter.entry.*;
import databute.databuter.entry.type.IntegerEntity;
import databute.databuter.entry.type.LongEntity;
import databute.databuter.entry.type.StringEntity;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class EntityOperationSuccessMessageHandler extends AbstractMessageHandler<Session, EntityOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationSuccessMessageHandler.class);

    public EntityOperationSuccessMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationSuccessMessage entityOperationSuccessMessage) {
        logger.debug("Handling entry operation success message {}", entityOperationSuccessMessage);

        final EntityMessageDispatcher dispatcher = Databuter.instance().entityMessageDispatcher();

        final String id = entityOperationSuccessMessage.id();
        final EntityCallback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            call(callback, entityOperationSuccessMessage);
        }
    }

    private void call(EntityCallback callback, EntityOperationSuccessMessage entityOperationSuccessMessage) {
        try {
            switch (entityOperationSuccessMessage.valueType()) {
                case INTEGER:
                    callIntegerEntity(callback, entityOperationSuccessMessage);
                    break;
                case LONG:
                    callLongEntity(callback, entityOperationSuccessMessage);
                    break;
                case STRING:
                    callStringEntity(callback, entityOperationSuccessMessage);
                    break;
            }
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    private void callIntegerEntity(EntityCallback callback,
                                   EntityOperationSuccessMessage entityOperationSuccessMessage)
            throws EmptyEntityKeyException {
        final EntityKey entityKey = new EntityKey(entityOperationSuccessMessage.key());
        final Integer value = (Integer) entityOperationSuccessMessage.value();
        final Instant createdTimestamp = entityOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entityOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entityOperationSuccessMessage.expirationTimestamp();

        final Entity entity = new IntegerEntity(entityKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entity);
    }

    private void callLongEntity(EntityCallback callback,
                                EntityOperationSuccessMessage entityOperationSuccessMessage)
            throws EmptyEntityKeyException {
        final EntityKey entityKey = new EntityKey(entityOperationSuccessMessage.key());
        final Long value = (Long) entityOperationSuccessMessage.value();
        final Instant createdTimestamp = entityOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entityOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entityOperationSuccessMessage.expirationTimestamp();

        final Entity entity = new LongEntity(entityKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entity);
    }

    private void callStringEntity(EntityCallback callback,
                                  EntityOperationSuccessMessage entityOperationSuccessMessage)
            throws EmptyEntityKeyException {
        final EntityKey entityKey = new EntityKey(entityOperationSuccessMessage.key());
        final String value = (String) entityOperationSuccessMessage.value();
        final Instant createdTimestamp = entityOperationSuccessMessage.createdTimestamp();
        final Instant lastUpdatedTimestamp = entityOperationSuccessMessage.lastUpdatedTimestamp();
        final Instant expirationTimestamp = entityOperationSuccessMessage.expirationTimestamp();

        final Entity entity = new StringEntity(entityKey, value, createdTimestamp, lastUpdatedTimestamp, expirationTimestamp);
        callback.onSuccess(entity);
    }
}
