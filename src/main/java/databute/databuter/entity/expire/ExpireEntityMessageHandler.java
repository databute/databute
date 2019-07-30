package databute.databuter.entity.expire;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.entity.*;
import databute.databuter.entity.result.fail.EntityOperationFailMessage;
import databute.databuter.entity.result.success.EntityOperationSuccessMessage;
import databute.databuter.network.Session;
import databute.databuter.network.message.AbstractMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ExpireEntityMessageHandler extends AbstractMessageHandler<Session, ExpireEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ExpireEntityMessageHandler.class);

    public ExpireEntityMessageHandler(Session session) {
        super(session);
    }

    @Override
    public void handle(ExpireEntityMessage expireEntityMessage) {
        logger.debug("Handling expire entity message {}", expireEntityMessage);

        final String id = expireEntityMessage.id();
        final String key = expireEntityMessage.key();
        final Instant expirationTimestamp = expireEntityMessage.expirationTimestamp();

        try {
            final EntityKey entityKey = new EntityKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entityKey);
            if (bucket == null) {
                // TODO(@ghkim3221): 키에 해당하는 버킷을 찾을 수 없는 경우. 이 경우가 발생할 것인가...?
            } else {
                bucket.expire(entityKey, expirationTimestamp, new EntityCallback() {
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
                            logger.error("Unknown error to expire entity {}", key, e);
                        }
                    }
                });
            }
        } catch (EmptyEntityKeyException e) {
            session().send(EntityOperationFailMessage.emptyKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
        }
    }
}
