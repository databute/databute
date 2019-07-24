package databute.databuter.client.network;

import com.google.common.collect.Maps;
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
        this.resolver = new ClientMessageCodeResolver();

        this.serializers = Maps.newHashMap();

        this.deserializers = Maps.newHashMap();
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
