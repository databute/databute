package databute.databuter.client.network;

import databute.databuter.network.AbstractSessionAcceptor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class ClientSessionAcceptor extends AbstractSessionAcceptor {

    public ClientSessionAcceptor() {
        super(new NioEventLoopGroup(), new NioEventLoopGroup());
    }

    @Override
    protected ChannelHandler childHandler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new ClientChannelHandler());
            }
        };
    }
}
