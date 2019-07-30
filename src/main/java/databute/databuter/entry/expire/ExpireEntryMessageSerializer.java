package databute.databuter.entity.expire;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntityMessageSerializer implements MessageSerializer<ExpireEntityMessage> {

    @Override
    public Packet serialize(ExpireEntityMessage expireEntityMessage) {
        checkNotNull(expireEntityMessage, "expireEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(expireEntityMessage.id());
        packet.writeString(expireEntityMessage.key());
        packet.writeLong(expireEntityMessage.expirationTimestamp().toEpochMilli());
        return packet;
    }
}
