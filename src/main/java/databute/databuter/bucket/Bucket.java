package databute.databuter.bucket;

import com.google.common.base.MoreObjects;
import databute.databuter.Databuter;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class Bucket {

    private final String id;

    private String masterClusterId;
    private String backupClusterId;

    public Bucket() {
        this.id = UUID.randomUUID().toString();
        this.masterClusterId = Databuter.instance().id();
    }

    public Bucket(BucketConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.masterClusterId = configuration.masterClusterId();
        this.backupClusterId = configuration.backupClusterId();
    }

    public String id() {
        return id;
    }

    public String backupClusterId() {
        return backupClusterId;
    }

    public Bucket backupClusterId(String backupClusterId) {
        this.backupClusterId = backupClusterId;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("masterClusterId", masterClusterId)
                .add("backupClusterId", backupClusterId)
                .toString();
    }
}
