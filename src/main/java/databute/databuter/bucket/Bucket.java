package databute.databuter.bucket;

import com.google.common.base.MoreObjects;
import databute.databuter.Databuter;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Bucket {

    private final String id;

    private String masterClusterId;
    private String backUpClusterId;

    public Bucket() {
        this.id = UUID.randomUUID().toString();
        this.masterClusterId = Databuter.instance().id();
    }

    public Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.masterClusterId = configuration.masterClusterId();
        this.backUpClusterId = configuration.backupClusterId();
    }

    public String id() {
        return id;
    }

    public String backUpClusterId() {
        return backUpClusterId;
    }

    public Bucket backUpClusterId(String backupClusterId) {
        this.backUpClusterId = backupClusterId;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("masterClusterId", masterClusterId)
                .add("backupClusterId", backUpClusterId)
                .toString();
    }
}
