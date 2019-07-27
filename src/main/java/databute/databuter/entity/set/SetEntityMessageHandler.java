package databute.databuter.entity.set;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
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
    }
}
