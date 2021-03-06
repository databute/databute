package databute.databutee.entry.expire;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntryMessageSerializer implements MessageSerializer<ExpireEntryMessage> {

    @Override
    public Packet serialize(ExpireEntryMessage expireEntryMessage) {
        checkNotNull(expireEntryMessage, "expireEntryMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(expireEntryMessage.id());
        packet.writeString(expireEntryMessage.key());
        if (expireEntryMessage.expirationTimestamp() == null) {
            packet.writeBoolean(false);
        } else {
            packet.writeBoolean(true);
            packet.writeLong(expireEntryMessage.expirationTimestamp().toEpochMilli());
        }
        return packet;
    }
}
