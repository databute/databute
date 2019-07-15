package databute.databuter.network.message;

import databute.databuter.network.packet.Packet;

public interface MessageDeserializer<M extends Message> {

    M deserialize(Packet packet);

}
