package databute.databuter.entity.delete;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
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
    }
}
