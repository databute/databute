package databute.databuter.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractSessionConnector implements SessionConnector {

    private SocketChannel channel;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private final EventLoopGroup loopGroup;

    protected AbstractSessionConnector(EventLoopGroup loopGroup) {
        this.loopGroup = checkNotNull(loopGroup, "loopGroup");
    }

    protected abstract ChannelHandler handler();

    protected SocketChannel channel() {
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
    public CompletableFuture<Void> connect(InetSocketAddress remoteAddress) {
        this.remoteAddress = checkNotNull(remoteAddress, "remoteAddress");

        final CompletableFuture<Void> future = new CompletableFuture<>();
        final Bootstrap bootstrap = new Bootstrap()
                .group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(handler());
        bootstrap.connect(remoteAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                try {
                    final SocketChannel channel = (SocketChannel) channelFuture.channel();
                    AbstractSessionConnector.this.channel = channel;
                    AbstractSessionConnector.this.localAddress = channel.localAddress();
                    AbstractSessionConnector.this.remoteAddress = channel.remoteAddress();

                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            } else {
                future.completeExceptionally(channelFuture.cause());
            }
        });

        return future;
    }
}
