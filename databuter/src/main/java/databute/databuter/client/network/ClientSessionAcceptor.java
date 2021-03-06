package databute.databuter.client.network;

import com.google.common.collect.Maps;
import databute.databuter.bucket.notification.BucketNotificationMessageSerializer;
import databute.databuter.client.register.RegisterMessageDeserializer;
import databute.databuter.cluster.notification.ClusterNodeNotificationMessageSerializer;
import databute.databuter.entry.delete.DeleteEntryMessageDeserializer;
import databute.databuter.entry.expire.ExpireEntryMessageDeserializer;
import databute.databuter.entry.get.GetEntryMessageDeserializer;
import databute.databuter.entry.result.fail.EntryOperationFailMessageSerializer;
import databute.databuter.entry.result.success.EntryOperationSuccessMessageSerializer;
import databute.databuter.entry.set.SetEntryMessageDeserializer;
import databute.databuter.entry.update.UpdateEntryMessageDeserializer;
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
        this.serializers.put(MessageCode.ENTRY_OPERATION_SUCCESS, new EntryOperationSuccessMessageSerializer());
        this.serializers.put(MessageCode.ENTRY_OPERATION_FAIL, new EntryOperationFailMessageSerializer());

        this.deserializers = Maps.newHashMap();
        this.deserializers.put(MessageCode.REGISTER, new RegisterMessageDeserializer());
        this.deserializers.put(MessageCode.GET_ENTRY, new GetEntryMessageDeserializer());
        this.deserializers.put(MessageCode.SET_ENTRY, new SetEntryMessageDeserializer());
        this.deserializers.put(MessageCode.UPDATE_ENTRY, new UpdateEntryMessageDeserializer());
        this.deserializers.put(MessageCode.DELETE_ENTRY, new DeleteEntryMessageDeserializer());
        this.deserializers.put(MessageCode.EXPIRE_ENTRY, new ExpireEntryMessageDeserializer());
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
