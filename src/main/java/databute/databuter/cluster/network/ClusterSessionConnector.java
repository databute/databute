package databute.databuter.cluster.network;

import databute.databuter.network.AbstractSessionConnector;
import databute.databuter.network.packet.codec.ByteToPacketDecoder;
import databute.databuter.network.packet.codec.PacketToByteEncoder;
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

                pipeline.addLast(new PacketToByteEncoder());
                pipeline.addLast(new ByteToPacketDecoder());

                pipeline.addLast(new OutboundClusterChannelHandler());
            }
        };
    }
}
