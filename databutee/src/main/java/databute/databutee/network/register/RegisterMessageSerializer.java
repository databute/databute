package databute.databutee.network.register;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterMessageSerializer implements MessageSerializer<RegisterMessage> {

    @Override
    public Packet serialize(RegisterMessage register) {
        checkNotNull(register, "register");

        final Packet packet = new BufferedPacket();
        return packet;
    }
}
