package databute.databuter.entry.expire;

import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntryMessageDeserializer implements MessageDeserializer<ExpireEntryMessage> {

    @Override
    public ExpireEntryMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        Instant expirationTimestamp = null;
        if (packet.readBoolean()) {
            expirationTimestamp = Instant.ofEpochMilli(packet.readLong());
        }
        return new ExpireEntryMessage(id, key, expirationTimestamp);
    }
}
