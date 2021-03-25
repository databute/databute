package databute.databuter.entry.get;

import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntryMessageDeserializer implements MessageDeserializer<GetEntryMessage> {

    @Override
    public GetEntryMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        return new GetEntryMessage(id, key);
    }
}
