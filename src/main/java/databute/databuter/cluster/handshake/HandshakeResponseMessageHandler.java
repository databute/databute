package databute.databuter.cluster.handshake;

import databute.databuter.cluster.network.ClusterMessageHandler;
import databute.databuter.cluster.network.ClusterSession;
import io.netty.channel.ChannelHandlerContext;

public class HandshakeResponseMessageHandler extends ClusterMessageHandler<HandshakeResponseMessage> {

    public HandshakeResponseMessageHandler(ClusterSession session) {
        super(session);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakeResponseMessage handshakeResponse) {

    }
}
