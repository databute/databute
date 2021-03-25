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

package databute.network;

import databute.network.message.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractSession implements Session {

    private final SocketChannel channel;
    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    protected AbstractSession(SocketChannel channel) {
        this.channel = checkNotNull(channel, "channel");
        localAddress = checkNotNull(channel.localAddress(), "localAddress");
        remoteAddress = checkNotNull(channel.remoteAddress(), "remoteAddress");
    }

    @Override
    public final SocketChannel channel() {
        return channel;
    }

    @Override
    public final InetSocketAddress localAddress() {
        return localAddress;
    }

    @Override
    public final InetSocketAddress remoteAddress() {
        return remoteAddress;
    }

    @Override
    public final ChannelFuture close() {
        return channel.close();
    }

    @Override
    public final ChannelFuture send(Message message) {
        checkNotNull(message, "message");
        return channel.writeAndFlush(message);
    }
}
