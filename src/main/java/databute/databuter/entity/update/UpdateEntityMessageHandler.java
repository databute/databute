package databute.databuter.entity.update;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entity.*;
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
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.get(entityKey, new EntityCallback() {
                    @Override
                    public void onSuccess(Entity entity) {
                        updateEntity(entity, updateEntityMessage);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof NotFoundException) {
                            session().send(EntityOperationFailMessage.notFound(id, key));
                        } else if (e instanceof EmptyEntityKeyException) {
                            session().send(EntityOperationFailMessage.emptyKey(id, key));
                        } else if (e instanceof DuplicateEntityKeyException) {
                            session().send(EntityOperationFailMessage.duplicateKey(id, key));
                        } else if (e instanceof UnsupportedValueTypeException) {
                            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
                        } else {
                            logger.error("Unknown error to get entity {}", key, e);
                        }
                    }
                });
            }
        } catch (EmptyEntityKeyException e) {
            session().send(EntityOperationFailMessage.emptyKey(id, key));
        }
    }

    private void updateEntity(Entity entity, UpdateEntityMessage updateEntityMessage) {
        if (entity instanceof IntegerEntity) {
            final IntegerEntity integerEntity = (IntegerEntity) entity;
            updateIntegerEntity(integerEntity, updateEntityMessage);
        } else if (entity instanceof LongEntity) {
            final LongEntity longEntity = (LongEntity) entity;
            updateLongEntity(longEntity, updateEntityMessage);
        } else if (entity instanceof StringEntity) {
            final StringEntity stringEntity = (StringEntity) entity;
            updateStringEntity(stringEntity, updateEntityMessage);
        }
    }

    private void updateIntegerEntity(IntegerEntity integerEntity, UpdateEntityMessage updateEntityMessage) {
        final Integer integerValue = (Integer) updateEntityMessage.value();
        integerEntity.set(integerValue);

        session().send(EntityOperationSuccessMessage.entity(updateEntityMessage.id(), integerEntity));
    }

    private void updateLongEntity(LongEntity longEntity, UpdateEntityMessage updateEntityMessage) {
        final Long longValue = (Long) updateEntityMessage.value();
        longEntity.set(longValue);

        session().send(EntityOperationSuccessMessage.entity(updateEntityMessage.id(), longEntity));
    }

    private void updateStringEntity(StringEntity stringEntity, UpdateEntityMessage updateEntityMessage) {
        final String stringValue = (String) updateEntityMessage.value();
        stringEntity.set(stringValue);

        session().send(EntityOperationSuccessMessage.entity(updateEntityMessage.id(), stringEntity));
    }
}
