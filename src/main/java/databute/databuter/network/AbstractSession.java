package databute.databuter.network;

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
        this.localAddress = checkNotNull(channel.localAddress(), "localAddress");
        this.remoteAddress = checkNotNull(channel.remoteAddress(), "remoteAddress");
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
}
