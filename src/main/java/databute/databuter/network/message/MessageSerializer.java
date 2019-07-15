package databute.databuter.network.message;

import databute.databuter.network.packet.Packet;

public interface MessageSerializer<M extends Message> {

    Packet serialize(M message);

}
