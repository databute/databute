package databute.databuter.cluster;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.node.AbstractClusterNode;
import databute.databuter.cluster.node.ClusterNodeConfiguration;

public class LocalClusterNode extends AbstractClusterNode {

    public LocalClusterNode(ClusterNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration())
                .toString();
    }
}
