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
import databute.network.AbstractSessionAcceptor;
import databute.network.message.MessageCode;
import databute.network.message.MessageDeserializer;
import databute.network.message.MessageSerializer;
import databute.network.message.codec.MessageToPacketEncoder;
import databute.network.message.codec.PacketToMessageDecoder;
import databute.network.packet.codec.ByteToPacketDecoder;
import databute.network.packet.codec.PacketToByteEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class ClientSessionAcceptor extends AbstractSessionAcceptor {

    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public ClientSessionAcceptor() {
        super(new NioEventLoopGroup(), new NioEventLoopGroup());

        serializers = Maps.newHashMap();
        serializers.put(MessageCode.CLUSTER_NODE_NOTIFICATION, new ClusterNodeNotificationMessageSerializer());
        serializers.put(MessageCode.BUCKET_NOTIFICATION, new BucketNotificationMessageSerializer());
        serializers.put(MessageCode.ENTRY_OPERATION_SUCCESS, new EntryOperationSuccessMessageSerializer());
        serializers.put(MessageCode.ENTRY_OPERATION_FAIL, new EntryOperationFailMessageSerializer());

        deserializers = Maps.newHashMap();
        deserializers.put(MessageCode.REGISTER, new RegisterMessageDeserializer());
        deserializers.put(MessageCode.GET_ENTRY, new GetEntryMessageDeserializer());
        deserializers.put(MessageCode.SET_ENTRY, new SetEntryMessageDeserializer());
        deserializers.put(MessageCode.UPDATE_ENTRY, new UpdateEntryMessageDeserializer());
        deserializers.put(MessageCode.DELETE_ENTRY, new DeleteEntryMessageDeserializer());
        deserializers.put(MessageCode.EXPIRE_ENTRY, new ExpireEntryMessageDeserializer());
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
                pipeline.addLast(new PacketToMessageDecoder(deserializers));

                pipeline.addLast(new ClientChannelHandler());
            }
        };
    }
}
