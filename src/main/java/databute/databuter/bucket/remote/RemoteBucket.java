package databute.databuter.bucket.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;

public class RemoteBucket extends Bucket {
    //TODO(@nono5546)
    public RemoteBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    public RemoteBucket() {
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
