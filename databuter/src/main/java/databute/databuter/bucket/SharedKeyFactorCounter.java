package databute.databuter.bucket;

import databute.databuter.Databuter;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.utils.ZKPaths;

public class SharedKeyFactorCounter {

    private final SharedCount sharedCount;

    public SharedKeyFactorCounter() {
        final String zooKeeperPath = Databuter.instance().configuration().zooKeeper().path();
        final String path = ZKPaths.makePath(zooKeeperPath, "keyFactor");
        this.sharedCount = new SharedCount(Databuter.instance().curator(), path, 0);
    }

    public void start() throws SharedKeyFactorException {
        try {
            sharedCount.start();
        } catch (Exception e) {
            throw new SharedKeyFactorException("Fail to start shared key factor.", e);
        }
    }

    public int getAndIncreaseSharedKeyFactor() throws SharedKeyFactorException {
        try {
            final int previousKeyFactor = sharedCount.getCount();
            sharedCount.setCount(previousKeyFactor + 1);
            return previousKeyFactor;
        } catch (Exception e) {
            throw new SharedKeyFactorException("Failed to increase shared key factor.", e);
        }
    }

    public int getAndDecreaseSharedKeyFactor() throws SharedKeyFactorException {
        try {
            final int previousKeyFactor = sharedCount.getCount();
            sharedCount.setCount(previousKeyFactor - 1);
            return previousKeyFactor;
        } catch (Exception e) {
            throw new SharedKeyFactorException("Failed to decrease shared key factor.", e);
        }
    }
}
