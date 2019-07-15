package databute.databuter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public final class Databuter {

    private static final Logger logger = LoggerFactory.getLogger(Databuter.class);
    private static final Databuter instance = new Databuter();

    private DatabuterConfiguration configuration;

    private Databuter() {
        super();
    }

    public static Databuter instance() {
        return instance;
    }

    private void start() throws IOException {
        logger.info("Starting Databuter at {}", Instant.now());

        loadConfiguration();
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

    public static void main(String[] args) {
        try {
            instance().start();
        } catch (Exception e) {
            logger.error("Failed to start Databuter.", e);
        }
    }
}
