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
import databute.network.message.MessageSerializer;
import databute.network.packet.BufferedPacket;
import databute.network.packet.Packet;
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
