package databute.databuter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketException;
import databute.databuter.bucket.BucketGroup;
import databute.databuter.client.network.ClientSessionAcceptor;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.ClusterException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public final class Databuter {

    private static final Logger logger = LoggerFactory.getLogger(Databuter.class);
    private static final Databuter instance = new Databuter();
    private static final BucketGroup bucketGroup = new BucketGroup();

    private DatabuterConfiguration configuration;
    private CuratorFramework curator;
    private Cluster cluster;
    private ClientSessionAcceptor clientAcceptor;

    private Databuter() {
        super();
    }

    public static Databuter instance() {
        return instance;
    }

    private void start() throws Exception {
        logger.info("Starting Databuter at {}", Instant.now());

        loadConfiguration();

        connectToZooKeeper();

        makeBucket();

        joinCluster();

        bindClientAcceptor();
    }

    private void loadConfiguration() throws IOException {
        logger.debug("Loading configuration...");

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final Path configurationPath = Paths.get(DatabuterConstants.CONFIGURATION_PATH);
        final File configurationFile = configurationPath.toFile();
        configuration = mapper.readValue(configurationFile, DatabuterConfiguration.class);
        logger.info("Loaded configuration from {}", configurationPath.toAbsolutePath());
        logger.debug("Loaded configuration: {}", configuration);
    }

    private void connectToZooKeeper() throws InterruptedException {
        final int baseSleepTimeMs = configuration.zooKeeper().baseSleepTimeMs();
        final int maxRetries = configuration.zooKeeper().maxRetries();
        curator = CuratorFrameworkFactory.builder()
                .connectString(configuration.zooKeeper().connectString())
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries))
                .build();
        curator.start();
        curator.blockUntilConnected();
        logger.debug("Connected with the ZooKeeper at {}.", curator.getZookeeperClient().getCurrentConnectionString());
    }

    private void joinCluster() throws ClusterException {
        logger.debug("Joining cluster...");

        cluster = new Cluster(configuration.cluster(), curator, bucketGroup);
        cluster.join();
    }

    private void makeBucket() throws BucketException {
        final long availableMemory = Runtime.getRuntime().totalMemory() - configuration.guardMemorySizeMb();
        final long bucketCount = availableMemory / configuration.bucketMemorySizeMb();

        logger.debug("Making {} bucket...", bucketCount);

        for (int i = 0; i < bucketCount; ++i) {
            final Bucket bucket = new Bucket();
            final boolean added = bucketGroup.add(bucket);
            if (!added) {
                throw new BucketException("Found duplcated bucket " + bucket);
            }
        }
    }

    private void bindClientAcceptor() {
        final String address = configuration.client().address();
        final int port = configuration.client().port();
        final InetSocketAddress localAddress = new InetSocketAddress(address, port);
        clientAcceptor = new ClientSessionAcceptor();
        clientAcceptor.bind(localAddress).join();
        logger.debug("Client session acceptor is bound on {}", clientAcceptor.localAddress());
    }

    public static void main(String[] args) {
        try {
            instance().start();
        } catch (Exception e) {
            logger.error("Failed to start Databuter.", e);
        }
    }
}
