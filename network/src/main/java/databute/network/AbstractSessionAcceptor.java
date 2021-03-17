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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractSessionAcceptor implements SessionAcceptor {

    private ServerSocketChannel serverChannel;
    private InetSocketAddress localAddress;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    protected AbstractSessionAcceptor(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        this.bossGroup = checkNotNull(bossGroup, "bossGroup");
        this.workerGroup = checkNotNull(workerGroup, "workerGroup");
    }

    protected abstract ChannelHandler childHandler();

    protected final ServerSocketChannel serverChannel() {
        return serverChannel;
    }

    @Override
    public final InetSocketAddress localAddress() {
        return localAddress;
    }

    @Override
    public CompletableFuture<Void> bind(InetSocketAddress localAddress) {
        this.localAddress = checkNotNull(localAddress, "localAddress");

        final CompletableFuture<Void> future = new CompletableFuture<>();
        new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(childHandler())
                .bind(localAddress)
                .addListener((ChannelFutureListener) channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        future.completeExceptionally(channelFuture.cause());
                        return;
                    }

                    try {
                        final ServerSocketChannel serverChannel = (ServerSocketChannel) channelFuture.channel();
                        this.serverChannel = serverChannel;
                        this.localAddress = serverChannel.localAddress();

                        future.complete(null);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }
}
