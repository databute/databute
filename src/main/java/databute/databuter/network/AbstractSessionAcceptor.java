package databute.databuter.network;

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
        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(childHandler());
        bootstrap.bind(localAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                try {
                    final ServerSocketChannel serverChannel = (ServerSocketChannel) channelFuture.channel();
                    AbstractSessionAcceptor.this.serverChannel = serverChannel;
                    AbstractSessionAcceptor.this.localAddress = serverChannel.localAddress();

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
