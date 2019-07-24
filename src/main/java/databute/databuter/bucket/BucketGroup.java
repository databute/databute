package databute.databuter.bucket;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketGroup {
    private static final Logger logger = LoggerFactory.getLogger(BucketGroup.class);

    private final Map<String, Bucket> buckets;

    public BucketGroup() {
        this.buckets = Maps.newConcurrentMap();
    }

    public boolean add(Bucket bucket) {
        checkNotNull(bucket, "remoteNode");

        final boolean added = (buckets.putIfAbsent(bucket.id(), bucket) == null);
        if (added) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added remote cluster node {}", bucket);
            } else {
                logger.info("Added remote cluster node {}", bucket.id());
            }
        }

        return added;
    }

    public boolean remove(Bucket bucket) {
        return remove(checkNotNull(bucket, "remoteNode").id());
    }

    public boolean remove(String id) {
        checkNotNull(id, "id");

        final Bucket remoteNode = buckets.remove(id);
        final boolean removed = (remoteNode != null);
        if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed remote cluster node {}", remoteNode);
            } else {
                logger.info("Removed remote cluster node {}", remoteNode.id());
            }
        }

        return removed;
    }
}
