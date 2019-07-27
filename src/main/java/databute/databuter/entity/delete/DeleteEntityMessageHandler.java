package databute.databuter.entity.delete;

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

public class DeleteEntityMessageHandler extends ClientMessageHandler<DeleteEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteEntityMessageHandler.class);

    public DeleteEntityMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(DeleteEntityMessage deleteEntityMessage) {
        logger.debug("Handling delete entity message {}", deleteEntityMessage);

        final String id = deleteEntityMessage.id();
        final String key = deleteEntityMessage.key();

        try {
            final EntityKey entityKey = new EntityKey(key);
            final Bucket bucket = Databuter.instance().bucketGroup().findByKey(entityKey);
            final Entity entity = bucket.remove(entityKey);
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
