/*
 * MIT License
 *
 * Copyright (c) 2019 - 2021 databute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package databute.network.packet;

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
        final byte[] bytes = new byte[length];
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
