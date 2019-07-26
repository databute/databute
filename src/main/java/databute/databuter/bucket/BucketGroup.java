package databute.databuter.bucket;

import com.google.common.collect.Maps;
import databute.databuter.Databuter;
import databute.databuter.bucket.notification.BucketNotificationMessage;
import databute.databuter.client.network.ClientSessionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketGroup implements Iterable<Bucket> {

    private static final Logger logger = LoggerFactory.getLogger(BucketGroup.class);

    private final Map<String, Bucket> buckets;

    public BucketGroup() {
        this.buckets = Maps.newConcurrentMap();
    }

    @Override
    public Iterator<Bucket> iterator() {
        return buckets.values().iterator();
    }

    public Bucket find(String id) {
        return buckets.get(id);
    }

    public boolean add(Bucket bucket) {
        checkNotNull(bucket, "bucket");

        // TODO(@ghkim3221): 중복되는 버킷을 발견한 경우 예외 throws
        final boolean added = (buckets.putIfAbsent(bucket.id(), bucket) == null);
        if (added) {
            broadcastBucketAdded(bucket);
        }

        return added;
    }

    private void broadcastBucketAdded(Bucket bucket) {
        final ClientSessionGroup clientSessionGroup = Databuter.instance().clientSessionGroup();
        clientSessionGroup.broadcastToListeningSession(BucketNotificationMessage.added()
                .id(bucket.id())
                .factor(bucket.factor())
                .activeNodeId(bucket.activeNodeId())
                .standbyNodeId(bucket.standbyNodeId())
                .build());
    }

    public boolean remove(Bucket bucket) {
        return remove(checkNotNull(bucket, "bucket").id());
    }

    public boolean remove(String id) {
        checkNotNull(id, "id");

        final Bucket bucket = buckets.remove(id);
        final boolean removed = (bucket != null);
        if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed bucket {}", bucket);
            } else {
                logger.info("Removed bucket {}", bucket.id());
            }

            broadcastBucketRemoved(bucket);
        }

        return removed;
    }

    private void broadcastBucketRemoved(Bucket bucket) {
        final ClientSessionGroup clientSessionGroup = Databuter.instance().clientSessionGroup();
        clientSessionGroup.broadcastToListeningSession(BucketNotificationMessage.removed()
                .id(bucket.id())
                .factor(bucket.factor())
                .activeNodeId(bucket.activeNodeId())
                .standbyNodeId(bucket.standbyNodeId())
                .build());
    }

    public boolean has(String id) {
        return buckets.containsKey(id);
    }
}
