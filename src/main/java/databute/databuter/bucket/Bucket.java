package databute.databuter.bucket;

import com.google.common.base.MoreObjects;
import databute.databuter.Databuter;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Bucket {

    private final String id;
    private final BucketConfiguration configuration;

    protected Bucket() {
        this(new BucketConfiguration(UUID.randomUUID().toString(), Databuter.instance().id()));
    }

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

    public String backUpClusterId() {
        return configuration.backupClusterId();
    }

    public Bucket backUpClusterId(String backupClusterId) {
        configuration.backupClusterId(backupClusterId);
        return this;
    }

    public void updateConfiguration(BucketConfiguration configuration) {
        configuration.update(configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .toString();
    }
}
