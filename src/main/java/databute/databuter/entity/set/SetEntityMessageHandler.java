package databute.databuter.entity.set;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import databute.databuter.entity.*;
import databute.databuter.entity.result.fail.EntityOperationFailMessage;
import databute.databuter.entity.result.success.EntityOperationSuccessMessage;
import databute.databuter.entity.type.IntegerEntity;
import databute.databuter.entity.type.LongEntity;
import databute.databuter.entity.type.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetEntityMessageHandler extends ClientMessageHandler<SetEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(SetEntityMessageHandler.class);

    public SetEntityMessageHandler(ClientSession session) {
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
            final Entity entity = bucket.get(entityKey);
            if (entity == null) {
                switch (setEntityMessage.valueType()) {
                    case INTEGER: {
                        final Integer integerValue = (Integer) setEntityMessage.value();
                        final IntegerEntity integerEntity = new IntegerEntity(entityKey, integerValue);

                        bucket.add(integerEntity);
                        session().send(EntityOperationSuccessMessage.entity(id, integerEntity));
                        break;
                    }
                    case LONG: {
                        final Long longValue = (Long) setEntityMessage.value();
                        final LongEntity longEntity = new LongEntity(entityKey, longValue);

                        bucket.add(longEntity);
                        session().send(EntityOperationSuccessMessage.entity(id, longEntity));
                        break;
                    }
                    case STRING: {
                        final String stringValue = (String) setEntityMessage.value();
                        final StringEntity stringEntity = new StringEntity(entityKey, stringValue);

                        bucket.add(stringEntity);
                        session().send(EntityOperationSuccessMessage.entity(id, stringEntity));
                        break;
                    }
                }
            } else {
                switch (setEntityMessage.valueType()) {
                    case INTEGER: {
                        if (entity instanceof IntegerEntity) {
                            final IntegerEntity integerEntity = (IntegerEntity) entity;

                            final Integer integerValue = (Integer) setEntityMessage.value();
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

                            final Long longValue = (Long) setEntityMessage.value();
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

                            final String stringValue = (String) setEntityMessage.value();
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
        } catch (DuplicateEntityKeyException e) {
            session().send(EntityOperationFailMessage.duplicateKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
        }
    }
}
