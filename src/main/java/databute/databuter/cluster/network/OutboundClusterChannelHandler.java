package databute.databuter.cluster.network;

import com.google.gson.Gson;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessage;
import databute.databuter.cluster.handshake.response.HandshakeResponseMessageHandler;
import databute.databuter.cluster.remote.RemoteClusterNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

public class OutboundClusterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OutboundClusterChannelHandler.class);

    private ClusterSession session;

    private final ClusterCoordinator clusterCoordinator;
    private final RemoteClusterNode remoteNode;

    public OutboundClusterChannelHandler(ClusterCoordinator clusterCoordinator, RemoteClusterNode remoteNode) {
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
        this.remoteNode = checkNotNull(remoteNode, "remoteNode");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new ClusterSession(channel, clusterCoordinator);
        logger.info("Active new cluster outbound session {}", session);

        remoteNode.session(session);

        configurePipeline(ctx);

        session.send(new HandshakeRequestMessage(remoteNode.id()));
    }

    private void configurePipeline(ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast(new HandshakeResponseMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster outbound session {}", session);

        clusterCoordinator.remoteNodeGroup().remove(remoteNode);

        for (Bucket bucket : Databuter.instance().bucketGroup()) {
            if (StringUtils.equals(bucket.activeNodeId(), Databuter.instance().id())) {
                if (StringUtils.equals(bucket.standbyNodeId(), remoteNode.id())) {
                    bucket.configuration().standbyNodeId(null);

                    ctx.executor().submit((Callable<Void>) () -> {
                        updateToZookeeper(bucket);
                        return null;
                    }).addListener(future -> {
                        if (future.isSuccess()) {
                            logger.debug("Sucess remove bucket {}.", bucket.id());
                        } else {
                            logger.error("Fail remove bucket {}.", bucket.id(), future.cause());
                        }
                    });
                }
            }
        }
    }

    private void updateToZookeeper(Bucket bucket) throws Exception {
        final String json2 = new Gson().toJson(bucket.configuration());
        final String zookeeperPath =Databuter.instance().configuration().zooKeeper().path();
        final String path = ZKPaths.makePath(zookeeperPath, "bucket", bucket.id());

        Databuter.instance().curator()
                .setData()
                .forPath(path, json2.getBytes());
    }
}
