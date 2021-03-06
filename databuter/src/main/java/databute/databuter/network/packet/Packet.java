package databute.databuter.network.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public interface Packet {

    int DEFAULT_INITIAL_CAPACITY = 64;

    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

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
