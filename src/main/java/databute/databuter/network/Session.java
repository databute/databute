package databute.databuter.network;

import databute.databuter.network.message.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

public interface Session {

    SocketChannel channel();

    InetSocketAddress localAddress();

    InetSocketAddress remoteAddress();

    ChannelFuture close();

    ChannelFuture send(Message message);

}
