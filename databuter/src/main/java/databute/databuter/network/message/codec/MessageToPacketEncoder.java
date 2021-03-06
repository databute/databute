package databute.databuter.network.message.codec;

import databute.databuter.network.message.Message;
import databute.databuter.network.message.MessageCode;
import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.packet.BufferedPacket;
import databute.databuter.network.packet.Packet;
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
