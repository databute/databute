package databute.databuter.bucket;

import databute.databuter.Databuter;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.utils.ZKPaths;

public class SharedKeyFactorCounter {

    private final SharedCount sharedKeyFactor;

    public SharedKeyFactorCounter() {
        final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
        final String path = ZKPaths.makePath(zooKeeperPath, "keyFactor");
        this.sharedKeyFactor = new SharedCount(Databuter.instance().curator(), path, 0);
    }

    public void start() throws BucketException {
        try {
            sharedKeyFactor.start();
        } catch (Exception e) {
            throw new BucketException("Fail to start shared key factor.", e);
        }
    }

    public int getAndIncreaseSharedKeyFactor() throws BucketException {
        try {
            final int previousKeyFactor = sharedKeyFactor.getCount();
            sharedKeyFactor.setCount(previousKeyFactor + 1);
            return previousKeyFactor;
        } catch (Exception e) {
            throw new BucketException("Failed to increase shared key factor.", e);
        }
    }
}
