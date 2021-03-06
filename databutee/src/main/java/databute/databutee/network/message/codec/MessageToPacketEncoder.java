package databute.databutee.network.message.codec;

import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;
import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class MessageToPacketEncoder extends MessageToMessageEncoder<Message> {

    private final Map<MessageCode, MessageSerializer> serializers;

    public MessageToPacketEncoder(Map<MessageCode, MessageSerializer> serializers) {
        this.serializers = checkNotNull(serializers, "serializers");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
        final MessageCode messageCode = message.messageCode();
        final MessageSerializer serializer = serializers.get(messageCode);
        checkNotNull(serializer, "serializer");

        final Packet packet = new BufferedPacket();
        packet.writeByte((byte) messageCode.value());
        packet.writeBytes(serializer.serialize(message).buf());
        out.add(packet);
    }
}
