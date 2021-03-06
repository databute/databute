package databute.databutee.network.packet.codec;

import databute.databutee.network.packet.BufferedPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToPacketDecoder extends ByteToMessageDecoder {

    private static final int SIZEOF_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        if (buf.readableBytes() < SIZEOF_LENGTH) {
            return;
        }

        buf.markReaderIndex();
        final int length = buf.readInt();
        if (buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }

        out.add(new BufferedPacket(buf.readBytes(length)));
    }
}
