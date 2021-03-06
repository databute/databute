package databute.databutee.network.message;

import databute.databutee.network.packet.Packet;

public interface MessageSerializer<M extends Message> {

    Packet serialize(M message);

}
