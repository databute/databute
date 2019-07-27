package databute.databuter.entity.get;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
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
    }
}
