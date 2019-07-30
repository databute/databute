package databute.databuter.entity.get;

import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessageDeserializer implements MessageDeserializer<GetEntityMessage> {

    @Override
    public GetEntityMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        return new GetEntityMessage(id, key);
    }
}
