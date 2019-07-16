package databute.databuter.cluster.coordinator;

import databute.databuter.cluster.ClusterException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(ClusterCoordinator.class);

    private final ClusterCoordinatorConfiguration configuration;

    private final CuratorFramework curator;

    public ClusterCoordinator(ClusterCoordinatorConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.curator = CuratorFrameworkFactory.builder()
                .connectString(configuration.connectString())
                .retryPolicy(new ExponentialBackoffRetry(configuration.baseSleepTimeMs(), configuration.maxRetries()))
                .build();
    }

    public void connect() throws ClusterException {
        try {
            logger.debug("Connecting to the ZooKeeper...");
            curator.start();
            curator.blockUntilConnected();
            logger.debug("Connected with the ZooKeeper.");
        } catch (InterruptedException e) {
            throw new ClusterException("Failed to connect to the ZooKeeper.", e);
        }
    }
}
