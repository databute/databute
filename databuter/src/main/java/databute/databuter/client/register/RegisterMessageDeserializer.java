package databute.databuter.client.register;

import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterMessageDeserializer implements MessageDeserializer<RegisterMessage> {

    @Override
    public RegisterMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        return new RegisterMessage();
    }
}
