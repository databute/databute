package databute.databutee.network.message.codec;

import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;
import databute.databutee.network.message.MessageCodeResolver;
import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class PacketToMessageDecoder extends MessageToMessageDecoder<Packet> {

    private final MessageCodeResolver resolver;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public PacketToMessageDecoder(MessageCodeResolver resolver, Map<MessageCode, MessageDeserializer> deserializers) {
        this.resolver = checkNotNull(resolver, "resolver");
        this.deserializers = checkNotNull(deserializers, "deserializers");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        final MessageCode messageCode = resolver.resolve(packet.readByte());
        checkNotNull(messageCode);

        final MessageDeserializer deserializer = deserializers.get(messageCode);
        checkNotNull(deserializer);

        final Message message = deserializer.deserialize(packet);
        out.add(message);
    }
}
