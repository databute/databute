package databute.databuter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import databute.databuter.bucket.BucketCoordinator;
import databute.databuter.bucket.BucketException;
import databute.databuter.bucket.BucketGroup;
import databute.databuter.client.ClientConfiguration;
import databute.databuter.client.network.ClientSessionAcceptor;
import databute.databuter.client.network.ClientSessionGroup;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.cluster.ClusterException;
import databute.databuter.entity.EntityMessageDispatcher;
import databute.databuter.network.Endpoint;
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
import java.util.UUID;

public final class Databuter {

    private static final Logger logger = LoggerFactory.getLogger(Databuter.class);
    private static final Databuter instance = new Databuter();

    private DatabuterConfiguration configuration;
    private CuratorFramework curator;
    private ClusterCoordinator clusterCoordinator;
    private BucketCoordinator bucketCoordinator;
    private ClientSessionAcceptor clientAcceptor;

    private final String id;
    private final BucketGroup bucketGroup;
    private final ClientSessionGroup clientSessionGroup;
    private final EntityMessageDispatcher entityMessageDispatcher;

    private Databuter() {
        this.id = UUID.randomUUID().toString();
        this.bucketGroup = new BucketGroup();
        this.clientSessionGroup = new ClientSessionGroup();
        this.entityMessageDispatcher = new EntityMessageDispatcher();
    }

    public static Databuter instance() {
        return instance;
    }

    public String id() {
        return id;
    }

    public BucketGroup bucketGroup() {
        return bucketGroup;
    }

    public ClientSessionGroup clientSessionGroup() {
        return clientSessionGroup;
    }

    public EntityMessageDispatcher entityMessageDispatcher() {
        return entityMessageDispatcher;
    }

    public DatabuterConfiguration configuration() {
        return configuration;
    }

    public CuratorFramework curator() {
        return curator;
    }

    public ClusterCoordinator clusterCoordinator() {
        return clusterCoordinator;
    }

    public BucketCoordinator bucketCoordinator() {
        return bucketCoordinator;
    }

    private void start() throws Exception {
        logger.info("Starting Databuter at {}", Instant.now());

        loadConfiguration();

        connectToZooKeeper();

        startBucketCoordinator();

        startClusterCoordinator();

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


    private void startBucketCoordinator() throws BucketException {
        bucketCoordinator = new BucketCoordinator();
        bucketCoordinator.start();
    }

    private void startClusterCoordinator() throws ClusterException {
        clusterCoordinator = new ClusterCoordinator(configuration.cluster(), id);
        clusterCoordinator.start();
    }

    private void bindClientAcceptor() {
        final ClientConfiguration clientConfiguration = configuration.client();
        final Endpoint clientEndpoint = clientConfiguration.endpoint();
        final String address = clientEndpoint.address();
        final int port = clientEndpoint.port();
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
