package databute.databuter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public final class Databuter {

    private static final Logger logger = LoggerFactory.getLogger(Databuter.class);
    private static final Databuter instance = new Databuter();

    private Databuter() {
        super();
    }

    public static Databuter instance() {
        return instance;
    }

    private void start() {
        logger.info("Starting Databuter at {}", Instant.now());
    }

    public static void main(String[] args) {
        try {
            instance().start();
        } catch (Exception e) {
            logger.error("Failed to start Databuter.", e);
        }
    }
}
