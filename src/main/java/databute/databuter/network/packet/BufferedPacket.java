package databute.databuter.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class BufferedPacket extends AbstractPacket {

    public BufferedPacket() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public BufferedPacket(int initialCapacity) {
        super(ByteBufAllocator.DEFAULT.buffer(initialCapacity));
    }

    public BufferedPacket(ByteBuf buf) {
        super(buf);
    }
}
