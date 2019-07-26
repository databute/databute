package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;

public class LocalBucket extends Bucket {
    //TODO(@nono5546)
    public LocalBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    public LocalBucket() {
        super();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
