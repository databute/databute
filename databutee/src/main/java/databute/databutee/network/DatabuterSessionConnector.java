package databute.databutee.network;

import com.google.common.collect.Maps;
import databute.databutee.Databutee;
import databute.databutee.bucket.notification.BucketNotificationMessageDeserializer;
import databute.databutee.entry.delete.DeleteEntryMessageSerializer;
import databute.databutee.entry.expire.ExpireEntryMessageSerializer;
import databute.databutee.entry.get.GetEntryMessageSerializer;
import databute.databutee.entry.result.fail.EntryOperationFailMessageDeserializer;
import databute.databutee.entry.result.success.EntryOperationSuccessMessageDeserializer;
import databute.databutee.entry.set.SetEntryMessageSerializer;
import databute.databutee.entry.update.UpdateEntryMessageSerializer;
import databute.databutee.network.register.RegisterMessageSerializer;
import databute.databutee.node.notification.NodeNotificationMessageDeserializer;
import databute.network.AbstractSessionConnector;
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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSessionConnector extends AbstractSessionConnector<DatabuterSession> {

    private final Databutee databutee;
    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public DatabuterSessionConnector(Databutee databutee, EventLoopGroup loopGroup) {
        super(loopGroup);
        this.databutee = checkNotNull(databutee, "databutee");

        serializers = Maps.newHashMap();
        serializers.put(MessageCode.REGISTER, new RegisterMessageSerializer());
        serializers.put(MessageCode.GET_ENTRY, new GetEntryMessageSerializer());
        serializers.put(MessageCode.SET_ENTRY, new SetEntryMessageSerializer());
        serializers.put(MessageCode.UPDATE_ENTRY, new UpdateEntryMessageSerializer());
        serializers.put(MessageCode.DELETE_ENTRY, new DeleteEntryMessageSerializer());
        serializers.put(MessageCode.EXPIRE_ENTRY, new ExpireEntryMessageSerializer());

        deserializers = Maps.newHashMap();
        deserializers.put(MessageCode.CLUSTER_NODE_NOTIFICATION, new NodeNotificationMessageDeserializer());
        deserializers.put(MessageCode.BUCKET_NOTIFICATION, new BucketNotificationMessageDeserializer());
        deserializers.put(MessageCode.ENTRY_OPERATION_SUCCESS, new EntryOperationSuccessMessageDeserializer());
        deserializers.put(MessageCode.ENTRY_OPERATION_FAIL, new EntryOperationFailMessageDeserializer());
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
                pipeline.addLast(new PacketToMessageDecoder(deserializers));

                pipeline.addLast(new DatabuterChannelHandler(databutee, sessionFuture()));
            }
        };
    }
}
