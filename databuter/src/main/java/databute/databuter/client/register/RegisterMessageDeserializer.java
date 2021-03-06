package databute.databuter.client.register;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterMessageDeserializer implements MessageDeserializer<RegisterMessage> {

    @Override
    public RegisterMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        return new RegisterMessage();
    }
}
