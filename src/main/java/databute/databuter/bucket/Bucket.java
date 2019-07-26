package databute.databuter.bucket;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Bucket {

    private final String id;
    private final BucketConfiguration configuration;

    protected Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.configuration = configuration;
    }

    public String id() {
        return id;
    }

    public BucketConfiguration configuration() {
        return configuration;
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
