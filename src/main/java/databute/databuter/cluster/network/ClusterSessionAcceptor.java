package databute.databuter.cluster.network;

import com.google.common.collect.Maps;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessageDeserializer;
import databute.databuter.cluster.handshake.response.HandshakeResponseMessageSerializer;
import databute.databuter.network.AbstractSessionAcceptor;
import databute.databuter.network.message.MessageCode;
import databute.databuter.network.message.MessageCodeResolver;
import databute.databuter.network.message.MessageDeserializer;
import databute.databuter.network.message.MessageSerializer;
import databute.databuter.network.message.codec.MessageToPacketEncoder;
import databute.databuter.network.message.codec.PacketToMessageDecoder;
import databute.databuter.network.packet.codec.ByteToPacketDecoder;
import databute.databuter.network.packet.codec.PacketToByteEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterSessionAcceptor extends AbstractSessionAcceptor {

    private final ClusterCoordinator clusterCoordinator;

    private final MessageCodeResolver resolver;
    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public ClusterSessionAcceptor(EventLoopGroup loopGroup, ClusterCoordinator clusterCoordinator) {
        super(loopGroup, loopGroup);
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
        this.resolver = new MessageCodeResolver();

        this.serializers = Maps.newHashMap();
        this.serializers.put(MessageCode.HANDSHAKE_RESPONSE, new HandshakeResponseMessageSerializer());

        this.deserializers = Maps.newHashMap();
        this.deserializers.put(MessageCode.HANDSHAKE_REQUEST, new HandshakeRequestMessageDeserializer());
    }

    @Override
    protected ChannelHandler childHandler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new PacketToByteEncoder());
                pipeline.addLast(new ByteToPacketDecoder());

                pipeline.addLast(new MessageToPacketEncoder(serializers));
                pipeline.addLast(new PacketToMessageDecoder(resolver, deserializers));

                pipeline.addLast(new InboundClusterChannelHandler(clusterCoordinator));
            }
        };
    }
}
