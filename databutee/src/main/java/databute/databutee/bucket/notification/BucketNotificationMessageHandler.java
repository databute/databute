package databute.databutee.bucket.notification;

import databute.databutee.bucket.Bucket;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import databute.databutee.node.DatabuterNode;
import databute.databutee.node.DatabuterNodeGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketNotificationMessageHandler extends MessageHandler<BucketNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(BucketNotificationMessageHandler.class);

    public BucketNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(BucketNotificationMessage bucketNotificationMessage) {
        switch (bucketNotificationMessage.type()) {
            case ADDED:
                addBucket(bucketNotificationMessage);
                break;
            case UPDATED:
                updateBucket(bucketNotificationMessage);
                break;
            case REMOVED:
                removeBucket(bucketNotificationMessage);
                break;
        }
    }

    private void addBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = new Bucket(bucketNotificationMessage.id())
                .keyFactor(bucketNotificationMessage.keyFactor())
                .activeNodeId(bucketNotificationMessage.activeNodeId())
                .standbyNodeId(bucketNotificationMessage.standbyNodeId());
        final boolean added = session().databutee().bucketGroup().add(bucket);
        if (added) {
            logger.debug("Added bucket {}", bucket);

            syncActiveDatabuterNode(bucket);
            syncStandbyDatabuterNode(bucket);
        }
    }

    private void updateBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = session().databutee().bucketGroup().find(bucketNotificationMessage.id());
        if (bucket == null) {
            logger.error("Failed to update bucket from notification message {}", bucketNotificationMessage);
        } else {
            final boolean updated = bucket.update(bucketNotificationMessage);
            if (updated) {
                logger.debug("Updated bucket {}", bucket);

                syncActiveDatabuterNode(bucket);
                syncStandbyDatabuterNode(bucket);
            }
        }
    }

    private void syncActiveDatabuterNode(Bucket bucket) {
        if (StringUtils.isEmpty(bucket.activeNodeId())) {
            return;
        }

        final DatabuterNodeGroup databuterNodeGroup = session().databutee().databuterNodeGroup();
        final DatabuterNode databuterNode = databuterNodeGroup.find(bucket.activeNodeId());
        if (databuterNode != null) {
            // 현재 Databuter 노드와 연결되어있지 않더라도, Databuter 노드가 연결될 때 버킷을 찾아 연결 할 것
            bucket.activeNode(databuterNode);
            logger.debug("Synchronized active databuter node {} to bucket {}", databuterNode.id(), bucket.id());
        }
    }

    private void syncStandbyDatabuterNode(Bucket bucket) {
        if (StringUtils.isEmpty(bucket.activeNodeId())) {
            return;
        }

        final DatabuterNodeGroup databuterNodeGroup = session().databutee().databuterNodeGroup();
        final DatabuterNode databuterNode = databuterNodeGroup.find(bucket.standbyNodeId());
        if (databuterNode != null) {
            // 현재 Databuter 노드와 연결되어있지 않더라도, Databuter 노드가 연결될 때 버킷을 찾아 연결 할 것
            bucket.standbyNode(databuterNode);
            logger.debug("Synchronized standby databuter node {} to bucket {}", databuterNode.id(), bucket.id());
        }
    }

    private void removeBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = session().databutee().bucketGroup().remove(bucketNotificationMessage.id());
        if (bucket == null) {
            logger.error("Failed to remove bucket from notification message {}", bucketNotificationMessage);
        } else {
            logger.debug("Removed bucket {}", bucket);
        }
    }
}
