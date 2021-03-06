package databute.databuter.cluster.remote;

import com.google.common.collect.Maps;
import databute.databuter.Databuter;
import databute.databuter.client.network.ClientSessionGroup;
import databute.databuter.cluster.notification.ClusterNodeNotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoteClusterNodeGroup implements Iterable<RemoteClusterNode> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteClusterNodeGroup.class);

    private final Map<String, RemoteClusterNode> remoteNodes;

    public RemoteClusterNodeGroup() {
        this.remoteNodes = Maps.newConcurrentMap();
    }

    @Override
    public Iterator<RemoteClusterNode> iterator() {
        return remoteNodes.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super RemoteClusterNode> action) {
        remoteNodes.values().forEach(action);
    }

    @Override
    public Spliterator<RemoteClusterNode> spliterator() {
        return remoteNodes.values().spliterator();
    }

    public boolean add(RemoteClusterNode remoteNode) {
        checkNotNull(remoteNode, "remoteNode");

        final boolean added = (remoteNodes.putIfAbsent(remoteNode.id(), remoteNode) == null);
        if (added) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added remote cluster node {}", remoteNode);
            } else {
                logger.info("Added remote cluster node {}", remoteNode.id());
            }

            broadcastRemoteNodeAdded(remoteNode);
        }

        return added;
    }

    private void broadcastRemoteNodeAdded(RemoteClusterNode remoteNode) {
        final ClientSessionGroup clientSessionGroup = Databuter.instance().clientSessionGroup();
        clientSessionGroup.broadcastToListeningSession(ClusterNodeNotificationMessage.added()
                .id(remoteNode.id())
                .endpoint(remoteNode.outboundEndpoint())
                .build());
    }

    public boolean remove(RemoteClusterNode remoteNode) {
        return remove(checkNotNull(remoteNode, "remoteNode").id());
    }

    public boolean remove(String id) {
        checkNotNull(id, "id");

        final RemoteClusterNode remoteNode = remoteNodes.remove(id);
        final boolean removed = (remoteNode != null);
        if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed remote cluster node {}", remoteNode);
            } else {
                logger.info("Removed remote cluster node {}", remoteNode.id());
            }

            broadcastRemoteNodeRemoved(remoteNode);
        }

        return removed;
    }

    private void broadcastRemoteNodeRemoved(RemoteClusterNode remoteNode) {
        final ClientSessionGroup clientSessionGroup = Databuter.instance().clientSessionGroup();
        clientSessionGroup.broadcastToListeningSession(ClusterNodeNotificationMessage.removed()
                .id(remoteNode.id())
                .endpoint(remoteNode.outboundEndpoint())
                .build());
    }
}
