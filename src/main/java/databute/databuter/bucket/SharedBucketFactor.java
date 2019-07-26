package databute.databuter.bucket;

import databute.databuter.Databuter;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.utils.ZKPaths;

public class SharedBucketFactor {

    private final SharedCount bucketFactor;

    public SharedBucketFactor() {
        final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
        final String path = ZKPaths.makePath(zooKeeperPath, "bucketFactor");
        this.bucketFactor = new SharedCount(Databuter.instance().curator(), path, 0);
    }

    public void start() throws BucketException {
        try {
            bucketFactor.start();
        } catch (Exception e) {
            throw new BucketException("Fail to start shared bucket factor.", e);
        }
    }

    public int getAndIncreaseBucketFactor() throws BucketException {
        try {
            final int previousBucketFactor = bucketFactor.getCount();
            bucketFactor.setCount(previousBucketFactor + 1);
            return previousBucketFactor;
        } catch (Exception e) {
            throw new BucketException("Failed to increase shared bucket factor.", e);
        }
    }
}
