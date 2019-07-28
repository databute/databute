package databute.databuter.entity.set;

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

public class SetEntityMessageHandler extends AbstractMessageHandler<Session, SetEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(SetEntityMessageHandler.class);

    public SetEntityMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(SetEntityMessage setEntityMessage) {
        logger.debug("Handling set entity message {}", setEntityMessage);

        final String id = setEntityMessage.id();
        final String key = setEntityMessage.key();

        try {
            final EntityKey entityKey = new EntityKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entityKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.get(entityKey, new EntityCallback() {
                    @Override
                    public void onSuccess(Entity entity) {
                        updateEntity(entity, setEntityMessage);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof NotFoundException) {
                            addEntity(bucket, setEntityMessage);
                        } else {
                            if (e instanceof EmptyEntityKeyException) {
                                session().send(EntityOperationFailMessage.emptyKey(id, key));
                            } else if (e instanceof DuplicateEntityKeyException) {
                                session().send(EntityOperationFailMessage.duplicateKey(id, key));
                            } else if (e instanceof UnsupportedValueTypeException) {
                                session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
                            } else {
                                logger.error("Unknown error to set entity {}", key, e);
                            }
                        }
                    }
                });
            }
        } catch (EmptyEntityKeyException e) {
            session().send(EntityOperationFailMessage.emptyKey(id, key));
        }
    }

    private void addEntity(Bucket bucket, SetEntityMessage setEntityMessage) {
        final String id = setEntityMessage.id();
        final String key = setEntityMessage.key();

        final EntityCallback callback = new EntityCallback() {
            @Override
            public void onSuccess(Entity entity) {
                session().send(EntityOperationSuccessMessage.entity(id, entity));
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
        };

        switch (setEntityMessage.valueType()) {
            case INTEGER:
                addIntegerEntity(bucket, setEntityMessage, callback);
                break;
            case LONG:
                addLongEntity(bucket, setEntityMessage, callback);
                break;
            case STRING:
                addStringEntity(bucket, setEntityMessage, callback);
                break;
        }
    }

    private void addIntegerEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final Integer integerValue = (Integer) setEntityMessage.value();
            final IntegerEntity integerEntity = new IntegerEntity(entityKey, integerValue);
            bucket.add(integerEntity, callback);
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    private void addLongEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final Long longValue = (Long) setEntityMessage.value();
            final LongEntity longEntity = new LongEntity(entityKey, longValue);
            bucket.add(longEntity, callback);
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    private void addStringEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final String stringValue = (String) setEntityMessage.value();
            final StringEntity stringEntity = new StringEntity(entityKey, stringValue);
            bucket.add(stringEntity, callback);
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    private void updateEntity(Entity entity, SetEntityMessage setEntityMessage) {
        if (entity instanceof IntegerEntity) {
            final IntegerEntity integerEntity = (IntegerEntity) entity;
            updateIntegerEntity(integerEntity, setEntityMessage);
        } else if (entity instanceof LongEntity) {
            final LongEntity longEntity = (LongEntity) entity;
            updateLongEntity(longEntity, setEntityMessage);
        } else if (entity instanceof StringEntity) {
            final StringEntity stringEntity = (StringEntity) entity;
            updateStringEntity(stringEntity, setEntityMessage);
        }
    }

    private void updateIntegerEntity(IntegerEntity integerEntity, SetEntityMessage setEntityMessage) {
        final Integer integerValue = (Integer) setEntityMessage.value();
        integerEntity.set(integerValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), integerEntity));
    }

    private void updateLongEntity(LongEntity longEntity, SetEntityMessage setEntityMessage) {
        final Long longValue = (Long) setEntityMessage.value();
        longEntity.set(longValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), longEntity));
    }

    private void updateStringEntity(StringEntity stringEntity, SetEntityMessage setEntityMessage) {
        final String stringValue = (String) setEntityMessage.value();
        stringEntity.set(stringValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), stringEntity));
    }
}
