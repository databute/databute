package databute.databuter.cluster.network;

import com.google.common.base.MoreObjects;
import databute.databuter.cluster.ClusterCoordinator;
import databute.network.AbstractSession;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;

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

    public <T> Future<T> submit(Callable<T> callable) {
        checkNotNull(callable, "callable");
        return channel().eventLoop().submit(callable);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel())
                .toString();
    }
}
