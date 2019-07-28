package databute.databuter.entity.set;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entity.*;
import databute.databuter.entity.result.fail.EntityOperationFailMessage;
import databute.databuter.entity.result.success.EntityOperationSuccessMessage;
import databute.databuter.entity.type.*;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            case LIST:
                addListEntity(bucket, setEntityMessage, callback);
                break;
            case SET:
                addSetEntity(bucket, setEntityMessage, callback);
                break;
            case DICTIONARY:
                addDictionaryEntity(bucket, setEntityMessage, callback);
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

    @SuppressWarnings("unchecked")
    private void addListEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final List<String> listValue = (List<String>) setEntityMessage.value();
            final ListEntity listEntity = new ListEntity(entityKey, listValue);
            bucket.add(listEntity, callback);
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addSetEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final Set<String> setValue = (Set<String>) setEntityMessage.value();
            final SetEntity setEntity = new SetEntity(entityKey, setValue);
            bucket.add(setEntity, callback);
        } catch (EmptyEntityKeyException e) {
            callback.onFailure(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addDictionaryEntity(Bucket bucket, SetEntityMessage setEntityMessage, EntityCallback callback) {
        try {
            final EntityKey entityKey = new EntityKey(setEntityMessage.key());
            final Map<String, String> dictionaryValue = (Map<String, String>) setEntityMessage.value();
            final DictionaryEntity dictionaryEntity = new DictionaryEntity(entityKey, dictionaryValue);
            bucket.add(dictionaryEntity, callback);
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
        } else if (entity instanceof ListEntity) {
            final ListEntity listEntity = (ListEntity) entity;
            updateListEntity(listEntity, setEntityMessage);
        } else if (entity instanceof SetEntity) {
            final SetEntity setEntity = (SetEntity) entity;
            updateSetEntity(setEntity, setEntityMessage);
        } else if (entity instanceof DictionaryEntity) {
            final DictionaryEntity dictionaryEntity = (DictionaryEntity) entity;
            updateDictionaryEntity(dictionaryEntity, setEntityMessage);
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

    @SuppressWarnings("unchecked")
    private void updateListEntity(ListEntity listEntity, SetEntityMessage setEntityMessage) {
        final List<String> listValue = (List<String>) setEntityMessage.value();
        listEntity.set(listValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), listEntity));
    }

    @SuppressWarnings("unchecked")
    private void updateSetEntity(SetEntity setEntity, SetEntityMessage setEntityMessage) {
        final Set<String> setValue = (Set<String>) setEntityMessage.value();
        setEntity.set(setValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), setEntity));
    }

    @SuppressWarnings("unchecked")
    private void updateDictionaryEntity(DictionaryEntity dictionaryEntity, SetEntityMessage setEntityMessage) {
        final Map<String, String> dictionaryValue = (Map<String, String>) setEntityMessage.value();
        dictionaryEntity.set(dictionaryValue);

        session().send(EntityOperationSuccessMessage.entity(setEntityMessage.id(), dictionaryEntity));
    }
}
