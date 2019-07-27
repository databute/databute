package databute.databuter.bucket;

import databute.databuter.entity.DuplicateEntityKeyException;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityKey;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Bucket {

    private final String id;
    private final BucketConfiguration configuration;

    protected Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.configuration = configuration;
    }

    public abstract Entity get(EntityKey entityKey);

    public abstract Entity add(Entity entity) throws DuplicateEntityKeyException;

    public abstract Entity remove(EntityKey entityKey);

    public String id() {
        return id;
    }

    public BucketConfiguration configuration() {
        return configuration;
    }

    public int factor() {
        return configuration.factor();
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
