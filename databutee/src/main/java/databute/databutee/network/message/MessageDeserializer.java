package databute.databutee.network.message;

import databute.databutee.network.packet.Packet;

public interface MessageDeserializer<M extends Message> {

    M deserialize(Packet packet);

}
