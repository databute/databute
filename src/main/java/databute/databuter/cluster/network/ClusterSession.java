package databute.databuter.cluster.network;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.ClusterCoordinator;
import databute.databuter.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterSession extends AbstractSession {

    private final ClusterCoordinator clusterCoordinator;

    public ClusterSession(SocketChannel channel, ClusterCoordinator clusterCoordinator) {
        super(channel);
        this.clusterCoordinator = checkNotNull(clusterCoordinator, "clusterCoordinator");
    }

    public ClusterCoordinator cluster() {
        return clusterCoordinator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .toString();
    }
}
