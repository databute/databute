package databute.databuter.entry.expire;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntryMessageSerializer implements MessageSerializer<ExpireEntryMessage> {

    @Override
    public Packet serialize(ExpireEntryMessage expireEntryMessage) {
        checkNotNull(expireEntryMessage, "expireEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(expireEntryMessage.id());
        packet.writeString(expireEntryMessage.key());
        packet.writeLong(expireEntryMessage.expirationTimestamp().toEpochMilli());
        return packet;
    }
}
