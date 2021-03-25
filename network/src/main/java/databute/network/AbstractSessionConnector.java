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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractSessionConnector<S extends Session> implements SessionConnector<S> {

    private SocketChannel channel;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private final EventLoopGroup loopGroup;
    private final CompletableFuture<S> sessionFuture = new CompletableFuture<>();


    protected AbstractSessionConnector(EventLoopGroup loopGroup) {
        this.loopGroup = checkNotNull(loopGroup, "loopGroup");
    }

    protected abstract ChannelHandler handler();

    protected final SocketChannel channel() {
        return channel;
    }

    protected final CompletableFuture<S> sessionFuture() {
        return sessionFuture;
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
    public CompletableFuture<S> connect(InetSocketAddress remoteAddress) {
        this.remoteAddress = checkNotNull(remoteAddress, "remoteAddress");

        new Bootstrap()
                .group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(handler())
                .connect(remoteAddress)
                .addListener((ChannelFutureListener) channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        sessionFuture.completeExceptionally(channelFuture.cause());
                        return;
                    }

                    try {
                        final SocketChannel channel = (SocketChannel) channelFuture.channel();
                        this.channel = channel;
                        localAddress = channel.localAddress();
                        this.remoteAddress = channel.remoteAddress();
                    } catch (Exception e) {
                        sessionFuture.completeExceptionally(e);
                    }
                });
        return sessionFuture;
    }
}
