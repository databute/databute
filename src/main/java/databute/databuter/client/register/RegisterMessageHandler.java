package databute.databuter.client.register;

import databute.databuter.client.network.ClientMessageHandler;
import databute.databuter.client.network.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterMessageHandler extends ClientMessageHandler<RegisterMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterMessageHandler.class);

    public RegisterMessageHandler(ClientSession session) {
        super(session);
    }

    @Override
    public void handle(RegisterMessage register) {
        logger.debug("Read register message: {}", register);
    }
}
