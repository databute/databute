package databute.databuter.bucket;

import databute.databuter.cluster.ClusterNode;
import databute.databuter.entry.Entry;
import databute.databuter.entry.EntryCallback;
import databute.databuter.entry.EntryKey;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Bucket {

    private ClusterNode activeNode;
    private ClusterNode standbyNode;

    private final String id;
    private final BucketConfiguration configuration;

    protected Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.configuration = configuration;
    }

    public abstract void get(EntryKey entryKey, EntryCallback callback);

    public abstract void add(Entry entry, EntryCallback callback);

    public abstract void remove(EntryKey entryKey, EntryCallback callback);

    public abstract void expire(EntryKey entryKey, Instant expirationTimestamp, EntryCallback callback);

    public String id() {
        return id;
    }

    public BucketConfiguration configuration() {
        return configuration;
    }

    public int keyFactor() {
        return configuration.keyFactor();
    }

    public String activeNodeId() {
        return configuration.activeNodeId();
    }

    public String standbyNodeId() {
        return configuration.standbyNodeId();
    }

    public Bucket activeNode(ClusterNode activeNode) {
        this.activeNode = activeNode;
        return this;
    }

    public Bucket standbyNode(ClusterNode standbyNode) {
        this.standbyNode = standbyNode;
        return this;
    }

    public void updateConfiguration(BucketConfiguration configuration) {
        this.configuration.update(checkNotNull(configuration, "configuration"));
    }
}
