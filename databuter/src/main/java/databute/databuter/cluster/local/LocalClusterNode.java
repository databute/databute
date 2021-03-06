package databute.databuter.cluster.local;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.AbstractClusterNode;
import databute.databuter.cluster.ClusterNodeConfiguration;

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
