package databute.databuter.cluster.network;

import databute.databuter.network.AbstractSessionAcceptor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class ClusterSessionAcceptor extends AbstractSessionAcceptor {

    public ClusterSessionAcceptor(EventLoopGroup loopGroup) {
        super(loopGroup, loopGroup);
    }

    @Override
    protected ChannelHandler childHandler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new InboundClusterChannelHandler());
            }
        };
    }
}
