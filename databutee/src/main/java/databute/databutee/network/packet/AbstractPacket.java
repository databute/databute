package databute.databutee.network.packet;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractPacket implements Packet {

    private final ByteBuf buf;

    protected AbstractPacket(ByteBuf buf) {
        this.buf = checkNotNull(buf, "buf");
    }

    @Override
    public ByteBuf buf() {
        return buf;
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

    @Override
    public void writeByte(byte b) {
        buf.writeByte(b);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeBytes(byte[] bytes) {
        if (bytes != null) {
            buf.writeBytes(bytes);
        }
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        if (buf != null) {
            this.buf.writeBytes(buf);
        }
    }

    @Override
    public short readShort() {
        return buf.readShort();
    }

    @Override
    public void writeShort(short s) {
        buf.writeShort(s);
    }

    @Override
    public int readInt() {
        return buf.readInt();
    }

    @Override
    public void writeInt(int i) {
        buf.writeInt(i);
    }

    @Override
    public long readLong() {
        return buf.readLong();
    }

    @Override
    public void writeLong(long l) {
        buf.writeLong(l);
    }

    @Override
    public boolean readBoolean() {
        return buf.readBoolean();
    }

    @Override
    public void writeBoolean(boolean b) {
        buf.writeBoolean(b);
    }

    @Override
    public String readString() {
        final int length = readInt();
        final byte[] bytes = readBytes(length);
        return new String(bytes, DEFAULT_CHARSET);
    }

    @Override
    public void writeString(String s) {
        if (StringUtils.isEmpty(s)) {
            writeInt(0);
        } else {
            final byte[] bytes = s.getBytes(DEFAULT_CHARSET);
            writeInt(bytes.length);
            writeBytes(bytes);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("buf", buf)
                .toString();
    }
}
