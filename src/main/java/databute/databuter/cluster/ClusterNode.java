package databute.databuter.cluster;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNode {

    private final String id;

    public ClusterNode(String id) {
        this.id = checkNotNull(id, "id");
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
