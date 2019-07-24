package databute.databuter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import databute.databuter.client.network.ClientSessionAcceptor;
import databute.databuter.cluster.Cluster;
import databute.databuter.cluster.ClusterException;
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

    private DatabuterConfiguration configuration;
    private Cluster cluster;
    private ClientSessionAcceptor clientAcceptor;

    private Databuter() {
        super();
    }

    public static Databuter instance() {
        return instance;
    }

    private void start() throws IOException, ClusterException {
        logger.info("Starting Databuter at {}", Instant.now());

        loadConfiguration();

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

    private void joinCluster() throws ClusterException {
        logger.debug("Joining cluster...");

        cluster = new Cluster(configuration.cluster());
        cluster.join();
    }

    private void bindClientAcceptor() {
        final InetSocketAddress localAddress = new InetSocketAddress(configuration.address(), configuration.port());
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
