package databute.databuter.entity.update;

import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateEntityMessageSerializer implements MessageSerializer<UpdateEntityMessage> {

    @Override
    public Packet serialize(UpdateEntityMessage updateEntityMessage) {
        checkNotNull(updateEntityMessage, "updateEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(updateEntityMessage.id());
        packet.writeString(updateEntityMessage.key());
        packet.writeString(updateEntityMessage.valueType().name());
        switch (updateEntityMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) updateEntityMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) updateEntityMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) updateEntityMessage.value();
                serializeStringValue(packet, stringValue);
            }
        }
        return packet;
    }

    private void serializeIntegerValue(Packet packet, Integer integerValue) {
        packet.writeInt(integerValue);
    }

    private void serializeLongValue(Packet packet, Long longValue) {
        packet.writeLong(longValue);
    }

    private void serializeStringValue(Packet packet, String stringValue) {
        packet.writeString(stringValue);
    }
}
