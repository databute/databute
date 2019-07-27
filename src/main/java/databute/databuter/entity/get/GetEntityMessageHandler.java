package databute.databuter.entity.get;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import databute.databuter.entity.EmptyEntityKeyException;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityKey;
import databute.databuter.entity.UnsupportedValueTypeException;
import databute.databuter.entity.result.fail.EntityOperationFailMessage;
import databute.databuter.entity.result.success.EntityOperationSuccessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEntityMessageHandler extends ClientMessageHandler<GetEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(GetEntityMessageHandler.class);

    public GetEntityMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(GetEntityMessage getEntityMessage) {
        logger.debug("Handling get entity message {}", getEntityMessage);

        final String id = getEntityMessage.id();
        final String key = getEntityMessage.key();

        try {
            final EntityKey entityKey = new EntityKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entityKey);
            final Entity entity = bucket.get(entityKey);
            if (entity == null) {
                session().send(EntityOperationFailMessage.notFound(id, key));
            } else {
                session().send(EntityOperationSuccessMessage.entity(id, entity));
            }
        } catch (EmptyEntityKeyException e) {
            session().send(EntityOperationFailMessage.emptyKey(id, key));
        } catch (UnsupportedValueTypeException e) {
            session().send(EntityOperationFailMessage.unsupportedValueType(id, key));
        }
    }
}
