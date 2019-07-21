package databute.databuter.cluster;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNodeGroup {

    private static final Logger logger = LoggerFactory.getLogger(ClusterNodeGroup.class);

    private final Map<String, ClusterNode> nodes;

    public ClusterNodeGroup() {
        this.nodes = Maps.newConcurrentMap();
    }

    public boolean add(ClusterNode node) {
        checkNotNull(node, "node");

        final boolean added = (nodes.putIfAbsent(node.id(), node) == null);
        if (added) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added cluster node {}", node);
            } else {
                logger.info("Added cluster node {}", node.id());
            }
        }

        return added;
    }
}
