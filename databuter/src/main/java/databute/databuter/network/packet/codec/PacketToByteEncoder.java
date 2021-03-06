package databute.databuter.network.packet.codec;

import databute.databuter.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketToByteEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        final ByteBuf buf = packet.buf();
        out.writeInt(buf.writerIndex());
        out.writeBytes(buf);
    }
}
