package databute.databuter.client.network;

import databute.databuter.network.message.AbstractMessageHandler;
import databute.databuter.network.message.Message;

public abstract class ClientMessageHandler<M extends Message> extends AbstractMessageHandler<ClientSession, M> {

    protected ClientMessageHandler(ClientSession session) {
        super(session);
    }
}
