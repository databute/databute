package databute.databuter.entity.expire;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntityMessageDeserializer implements MessageDeserializer<ExpireEntityMessage> {

    @Override
    public ExpireEntityMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final Instant expirationTimestamp = Instant.ofEpochMilli(packet.readLong());
        return new ExpireEntityMessage(id, key, expirationTimestamp);
    }
}
