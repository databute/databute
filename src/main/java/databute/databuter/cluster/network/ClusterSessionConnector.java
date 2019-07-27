package databute.databuter.cluster.network;

import com.google.common.collect.Maps;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessageSerializer;
import databute.databuter.cluster.handshake.response.HandshakeResponseMessageDeserializer;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.network.AbstractSessionConnector;
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

public class ClusterSessionConnector extends AbstractSessionConnector {

    private final ClusterCoordinator clusterCoordinator;
    private final RemoteClusterNode remoteNode;

    private final MessageCodeResolver resolver;
    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public ClusterSessionConnector(EventLoopGroup loopGroup, ClusterCoordinator clusterCoordinator, RemoteClusterNode remoteNode) {
        super(loopGroup);
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
        this.remoteNode = checkNotNull(remoteNode, "remoteNode");
        this.resolver = new MessageCodeResolver();

        this.serializers = Maps.newHashMap();
        this.serializers.put(MessageCode.HANDSHAKE_REQUEST, new HandshakeRequestMessageSerializer());

        this.deserializers = Maps.newHashMap();
        this.deserializers.put(MessageCode.HANDSHAKE_RESPONSE, new HandshakeResponseMessageDeserializer());
    }

    @Override
    protected ChannelHandler handler() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                final ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast(new PacketToByteEncoder());
                pipeline.addLast(new ByteToPacketDecoder());

                pipeline.addLast(new MessageToPacketEncoder(serializers));
                pipeline.addLast(new PacketToMessageDecoder(resolver, deserializers));

                pipeline.addLast(new OutboundClusterChannelHandler(clusterCoordinator, remoteNode));
            }
        };
    }
}
