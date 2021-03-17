package databute.databutee.network.register;

import databute.network.message.MessageSerializer;
import databute.network.packet.BufferedPacket;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterMessageSerializer implements MessageSerializer<RegisterMessage> {

    @Override
    public Packet serialize(RegisterMessage register) {
        checkNotNull(register, "register");

        return new BufferedPacket();
    }
}
