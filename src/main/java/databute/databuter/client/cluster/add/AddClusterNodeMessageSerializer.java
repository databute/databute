package databute.databuter.client.cluster.add;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddClusterNodeMessageSerializer implements MessageSerializer<AddClusterNodeMessage> {

    @Override
    public Packet serialize(AddClusterNodeMessage addClusterNodeMessage) {
        checkNotNull(addClusterNodeMessage, "addClusterNodeMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(addClusterNodeMessage.id());
        packet.writeString(addClusterNodeMessage.address());
        packet.writeInt(addClusterNodeMessage.port());
        return packet;
    }
}
