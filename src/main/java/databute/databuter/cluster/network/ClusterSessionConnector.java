package databute.databuter.cluster.network;

import databute.databuter.network.AbstractSessionConnector;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class ClusterSessionConnector extends AbstractSessionConnector {

    public ClusterSessionConnector(EventLoopGroup loopGroup) {
        super(loopGroup);
    }

    @Override
    protected ChannelHandler handler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new OutboundClusterChannelHandler());
            }
        };
    }
}
