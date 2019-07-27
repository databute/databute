package databute.databuter.entity.update;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateEntityMessageHandler extends ClientMessageHandler<UpdateEntityMessage> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateEntityMessageHandler.class);

    public UpdateEntityMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(UpdateEntityMessage updateEntityMessage) {
        logger.debug("handling update entity message {}", updateEntityMessage);
    }
}
