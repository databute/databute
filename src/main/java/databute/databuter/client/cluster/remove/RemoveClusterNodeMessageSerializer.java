package databute.databuter.client.cluster.remove;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoveClusterNodeMessageSerializer implements MessageSerializer<RemoveClusterNodeMessage> {

    @Override
    public Packet serialize(RemoveClusterNodeMessage removeClusterNodeMessage) {
        checkNotNull(removeClusterNodeMessage, "removeClusterNodeMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(removeClusterNodeMessage.id());
        return packet;
    }
}
