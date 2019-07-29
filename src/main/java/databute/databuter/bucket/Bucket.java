package databute.databuter.bucket;

import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityCallback;
import databute.databuter.entity.EntityKey;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Bucket {

    private final String id;
    private final BucketConfiguration configuration;

    protected Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.configuration = configuration;
    }

    public abstract void get(EntityKey entityKey, EntityCallback callback);

    public abstract void add(Entity entity, EntityCallback callback);

    public abstract void remove(EntityKey entityKey, EntityCallback callback);

    public abstract void expire(EntityKey entityKey, Instant expirationTimestamp, EntityCallback callback);

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

    public void updateConfiguration(BucketConfiguration configuration) {
        this.configuration.update(checkNotNull(configuration, "configuration"));
    }
}
