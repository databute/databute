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

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Packet {

    int DEFAULT_INITIAL_CAPACITY = 64;

    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    ByteBuf buf();

    byte readByte();

    void writeByte(byte b);

    byte[] readBytes(int length);

    void writeBytes(byte[] bytes);

    void writeBytes(ByteBuf buf);

    short readShort();

    void writeShort(short s);

    int readInt();

    void writeInt(int i);

    long readLong();

    void writeLong(long l);

    boolean readBoolean();

    void writeBoolean(boolean b);

    String readString();

    void writeString(String s);

}
