package databute.databuter.network;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

public interface Session {

    SocketChannel channel();

    InetSocketAddress localAddress();

    InetSocketAddress remoteAddress();

    ChannelFuture close();

}
