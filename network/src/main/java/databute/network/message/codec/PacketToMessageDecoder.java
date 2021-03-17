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

package databute.network.message.codec;

import databute.network.message.Message;
import databute.network.message.MessageCode;
import databute.network.message.MessageDeserializer;
import databute.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class PacketToMessageDecoder extends MessageToMessageDecoder<Packet> {

    private final Map<MessageCode, MessageDeserializer> deserializers;

    public PacketToMessageDecoder(Map<MessageCode, MessageDeserializer> deserializers) {
        this.deserializers = checkNotNull(deserializers, "deserializers");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
        final MessageCode messageCode = MessageCode.of(packet.readByte());

        final MessageDeserializer deserializer = deserializers.get(messageCode);
        checkNotNull(deserializer);

        final Message message = deserializer.deserialize(packet);
        out.add(message);
    }
}
