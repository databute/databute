package databute.databuter.cluster.network;

import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketCoordinator;
import databute.databuter.bucket.local.LocalBucket;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.handshake.request.HandshakeRequestMessage;
import databute.databuter.cluster.handshake.response.HandshakeResponseMessageHandler;
import databute.databuter.cluster.remote.RemoteClusterNode;
import databute.databuter.entity.result.fail.EntityOperationFailMessageHandler;
import databute.databuter.entity.result.success.EntityOperationSuccessMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        pipeline.addLast(new EntityOperationSuccessMessageHandler(session));
        pipeline.addLast(new EntityOperationFailMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Inactive cluster outbound session {}", session);

        clusterCoordinator.remoteNodeGroup().remove(remoteNode);

        //TODO(@nono5546):BucketCoordinator가 하도록 리팩토링.
        for (Bucket bucket : Databuter.instance().bucketGroup()) {
            if (bucket instanceof LocalBucket) {
                final LocalBucket localBucket = (LocalBucket) bucket;

                final boolean isActiveByLocalNode = localBucket.configuration().isActiveBy(Databuter.instance().id());
                final boolean isStandbyByLocalNode = localBucket.configuration().isStandbyBy(Databuter.instance().id());
                final boolean isActiveByRemoteNode = localBucket.configuration().isActiveBy(remoteNode.id());
                final boolean isStandbyByRemoteNode = localBucket.configuration().isStandbyBy(remoteNode.id());

                if (isActiveByLocalNode && isStandbyByRemoteNode) {
                    removeStanbyNode(bucket, localBucket);
                } else if (isActiveByRemoteNode && isStandbyByLocalNode) {
                    failover(localBucket);
                }
            }
        }
    }

    private void removeStanbyNode(Bucket bucket, LocalBucket localBucket) {
        final BucketCoordinator bucketCoordinator = Databuter.instance().bucketCoordinator();
        session.submit(() -> {
            localBucket.configuration().standbyNodeId(null);
            bucketCoordinator.update(localBucket);
            return null;
        }).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("Removed standby node of bucket {}.", localBucket.id());
            } else {
                logger.error("Failed to remove standby node of bucket {}", bucket.id(), future.cause());
            }
        });
    }

    private void failover(LocalBucket localBucket) {
        final BucketCoordinator bucketCoordinator = Databuter.instance().bucketCoordinator();
        final String standbyNodeId = localBucket.configuration().standbyNodeId();
        session.submit(() -> {
            localBucket.configuration().activeNodeId(standbyNodeId);
            localBucket.configuration().standbyNodeId(null);
            bucketCoordinator.update(localBucket);
            return null;
        }).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("Failover bucket {}.", localBucket.id());
            } else {
                logger.error("Failed to failover bucket {}.", localBucket.id(), future.cause());
            }
        });
    }
}