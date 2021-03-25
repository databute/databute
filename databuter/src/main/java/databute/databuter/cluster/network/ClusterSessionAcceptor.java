package databute.databuter.cluster.network;

import com.google.common.collect.Maps;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessageDeserializer;
import databute.databuter.cluster.handshake.response.HandshakeResponseMessageSerializer;
import databute.databuter.entry.delete.DeleteEntryMessageDeserializer;
import databute.databuter.entry.delete.DeleteEntryMessageSerializer;
import databute.databuter.entry.expire.ExpireEntryMessageDeserializer;
import databute.databuter.entry.expire.ExpireEntryMessageSerializer;
import databute.databuter.entry.get.GetEntryMessageDeserializer;
import databute.databuter.entry.get.GetEntryMessageSerializer;
import databute.databuter.entry.result.fail.EntryOperationFailMessageDeserializer;
import databute.databuter.entry.result.fail.EntryOperationFailMessageSerializer;
import databute.databuter.entry.result.success.EntryOperationSuccessMessageDeserializer;
import databute.databuter.entry.result.success.EntryOperationSuccessMessageSerializer;
import databute.databuter.entry.set.SetEntryMessageDeserializer;
import databute.databuter.entry.set.SetEntryMessageSerializer;
import databute.databuter.entry.update.UpdateEntryMessageDeserializer;
import databute.databuter.entry.update.UpdateEntryMessageSerializer;
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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterSessionAcceptor extends AbstractSessionAcceptor {

    private final ClusterCoordinator clusterCoordinator;

    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public ClusterSessionAcceptor(EventLoopGroup loopGroup, ClusterCoordinator clusterCoordinator) {
        super(loopGroup, loopGroup);
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");

        serializers = Maps.newHashMap();
        serializers.put(MessageCode.HANDSHAKE_RESPONSE, new HandshakeResponseMessageSerializer());
        serializers.put(MessageCode.GET_ENTRY, new GetEntryMessageSerializer());
        serializers.put(MessageCode.SET_ENTRY, new SetEntryMessageSerializer());
        serializers.put(MessageCode.UPDATE_ENTRY, new UpdateEntryMessageSerializer());
        serializers.put(MessageCode.DELETE_ENTRY, new DeleteEntryMessageSerializer());
        serializers.put(MessageCode.ENTRY_OPERATION_SUCCESS, new EntryOperationSuccessMessageSerializer());
        serializers.put(MessageCode.ENTRY_OPERATION_FAIL, new EntryOperationFailMessageSerializer());
        serializers.put(MessageCode.EXPIRE_ENTRY, new ExpireEntryMessageSerializer());

        deserializers = Maps.newHashMap();
        deserializers.put(MessageCode.HANDSHAKE_REQUEST, new HandshakeRequestMessageDeserializer());
        deserializers.put(MessageCode.GET_ENTRY, new GetEntryMessageDeserializer());
        deserializers.put(MessageCode.SET_ENTRY, new SetEntryMessageDeserializer());
        deserializers.put(MessageCode.UPDATE_ENTRY, new UpdateEntryMessageDeserializer());
        deserializers.put(MessageCode.DELETE_ENTRY, new DeleteEntryMessageDeserializer());
        deserializers.put(MessageCode.ENTRY_OPERATION_SUCCESS,
                new EntryOperationSuccessMessageDeserializer());
        deserializers.put(MessageCode.ENTRY_OPERATION_FAIL, new EntryOperationFailMessageDeserializer());
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

                pipeline.addLast(new InboundClusterChannelHandler(clusterCoordinator));
            }
        };
    }
}
