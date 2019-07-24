package databute.databuter.client.network;

import databute.databuter.network.message.MessageCode;
import databute.databuter.network.message.MessageCodeResolver;

public class ClientMessageCodeResolver implements MessageCodeResolver {

    @Override
    public MessageCode resolve(int value) {
        for (ClientMessageCode messageCode : ClientMessageCode.values()) {
            if (messageCode.value() == value) {
                return messageCode;
            }
        }
        return null;
    }
}
