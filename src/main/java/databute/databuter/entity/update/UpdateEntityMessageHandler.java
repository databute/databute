package databute.databuter.entity.update;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entity.EmptyEntityKeyException;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityKey;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.entity.result.fail.EntityOperationFailMessage;
import databute.databuter.entity.result.success.EntityOperationSuccessMessage;
import databute.databuter.entity.type.IntegerEntity;
import databute.databuter.entity.type.LongEntity;
import databute.databuter.entity.type.StringEntity;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateEntityMessageHandler extends AbstractMessageHandler<Session, UpdateEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateEntityMessageHandler.class);

    public UpdateEntityMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(UpdateEntityMessage updateEntityMessage) {
        logger.debug("handling update entity message {}", updateEntityMessage);

        final String id = updateEntityMessage.id();
        final String key = updateEntityMessage.key();

        try {
            final EntityKey entityKey = new EntityKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entityKey);
            final Entity entity = bucket.get(entityKey);
            if (entity == null) {
                session().send(EntityOperationFailMessage.notFound(id, key));
            } else {
                switch (updateEntityMessage.valueType()) {
                    case INTEGER: {
                        if (entity instanceof IntegerEntity) {
                            final IntegerEntity integerEntity = (IntegerEntity) entity;

                            final Integer integerValue = (Integer) updateEntityMessage.value();
                            integerEntity.set(integerValue);

                            session().send(EntityOperationSuccessMessage.entity(id, integerEntity));
                        } else {
                            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
                        }
                        break;
                    }
                    case LONG: {
                        if (entity instanceof LongEntity) {
                            final LongEntity longEntity = (LongEntity) entity;

                            final Long longValue = (Long) updateEntityMessage.value();
                            longEntity.set(longValue);

                            session().send(EntityOperationSuccessMessage.entity(id, longEntity));
                        } else {
                            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
                        }
                        break;
                    }
                    case STRING: {
                        if (entity instanceof StringEntity) {
                            final StringEntity stringEntity = (StringEntity) entity;

                            final String stringValue = (String) updateEntityMessage.value();
                            stringEntity.set(stringValue);

                            session().send(EntityOperationSuccessMessage.entity(id, stringEntity));
                        } else {
                            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
                        }
                        break;
                    }
                }
            }
        } catch (EmptyEntityKeyException e) {
            session().send(EntityOperationFailMessage.emptyKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
        }
    }
}
