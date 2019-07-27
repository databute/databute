package databute.databuter.client.network;

import com.google.common.collect.Maps;
import databute.databuter.bucket.notification.BucketNotificationMessageSerializer;
import databute.databuter.client.register.RegisterMessageDeserializer;
import databute.databuter.cluster.notification.ClusterNodeNotificationMessageSerializer;
import databute.databuter.entity.delete.DeleteEntityMessageDeserializer;
import databute.databuter.entity.get.GetEntityMessageDeserializer;
import databute.databuter.entity.set.SetEntityMessageDeserializer;
import databute.databuter.entity.update.UpdateEntityMessageDeserializer;
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
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class ClientSessionAcceptor extends AbstractSessionAcceptor {

    private final MessageCodeResolver resolver;
    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public ClientSessionAcceptor() {
        super(new NioEventLoopGroup(), new NioEventLoopGroup());
        this.resolver = new MessageCodeResolver();

        this.serializers = Maps.newHashMap();
        this.serializers.put(MessageCode.CLUSTER_NODE_NOTIFICATION, new ClusterNodeNotificationMessageSerializer());
        this.serializers.put(MessageCode.BUCKET_NOTIFICATION, new BucketNotificationMessageSerializer());

        this.deserializers = Maps.newHashMap();
        this.deserializers.put(MessageCode.REGISTER, new RegisterMessageDeserializer());
        this.deserializers.put(MessageCode.GET_ENTITY, new GetEntityMessageDeserializer());
        this.deserializers.put(MessageCode.SET_ENTITY, new SetEntityMessageDeserializer());
        this.deserializers.put(MessageCode.UPDATE_ENTITY, new UpdateEntityMessageDeserializer());
        this.deserializers.put(MessageCode.DELETE_ENTITY, new DeleteEntityMessageDeserializer());
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

                pipeline.addLast(new ClientChannelHandler());
            }
        };
    }
}
