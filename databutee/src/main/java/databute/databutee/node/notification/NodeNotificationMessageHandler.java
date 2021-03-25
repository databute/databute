package databute.databutee.node.notification;

import databute.databutee.Databutee;
import databute.databutee.bucket.Bucket;
import databute.databutee.bucket.BucketGroup;
import databute.databutee.network.DatabuterMessageHandler;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.DatabuterSessionConnector;
import databute.databutee.node.DatabuterNode;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NodeNotificationMessageHandler extends DatabuterMessageHandler<NodeNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(NodeNotificationMessageHandler.class);

    public NodeNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(NodeNotificationMessage nodeNotificationMessage) {
        switch (nodeNotificationMessage.type()) {
            case ADDED:
                addNode(nodeNotificationMessage);
                break;
            case REMOVED:
                removeNode(nodeNotificationMessage);
                break;
        }
    }

    private void addNode(NodeNotificationMessage nodeNotificationMessage) {
        final DatabuterNode node = DatabuterNode.builder()
                .id(nodeNotificationMessage.id())
                .address(nodeNotificationMessage.address())
                .port(nodeNotificationMessage.port())
                .build();
        final boolean added = session().databutee().databuterNodeGroup().add(node);
        if (added) {
            logger.debug("Added databuter node {}", node);

            syncBuckets(node);

            connectToNode(node);
        }
    }

    private void connectToNode(DatabuterNode node) {
        final Databutee databutee = session().databutee();
        final EventLoopGroup loopGroup = databutee.configuration().loopGroup();

        final String address = node.address();
        final int port = node.port();
        final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
        final DatabuterSessionConnector connector = new DatabuterSessionConnector(databutee, loopGroup);
        connector.connect(remoteAddress)
                .thenAccept(nodeSession -> {
                    logger.info("Connected with Databuter node {} at {}", node.id(), remoteAddress);

                    node.session(nodeSession);
                })
                .exceptionally(e -> {
                    logger.error("Failed to connect to Databuter node {}.", node.id(), e);
                    return null;
                });
    }

    private void syncBuckets(DatabuterNode node) {
        final String nodeId = node.id();
        final BucketGroup bucketGroup = session().databutee().bucketGroup();
        for (Bucket bucket : bucketGroup) {
            if (bucket.isActiveBy(nodeId)) {
                bucket.activeNode(node);
                logger.debug("Synchronized active databuter node {} to bucket {}", node.id(), bucket.id());
            }
            if (bucket.isStandbyBy(nodeId)) {
                bucket.standbyNode(node);
                logger.debug("Synchronized standby databuter node {} to bucket {}", node.id(), bucket.id());
            }
        }
    }

    private void removeNode(NodeNotificationMessage nodeNotificationMessage) {
        final DatabuterNode node = session().databutee().databuterNodeGroup().remove(nodeNotificationMessage.id());
        if (node == null) {
            logger.error("Failed to remove node from notification message {}", nodeNotificationMessage);
        } else {
            logger.debug("Removed node {}", node);
        }
    }
}
