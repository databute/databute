package databute.databuter.cluster;

import databute.databuter.network.Endpoint;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractClusterNode implements ClusterNode {

    private final String id;
    private final ClusterNodeConfiguration configuration;

    protected AbstractClusterNode(ClusterNodeConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        this.id = configuration.id();
        this.configuration = configuration;
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final ClusterNodeConfiguration configuration() {
        return configuration;
    }

    @Override
    public Endpoint inboundEndpoint() {
        return configuration.inboundEndpoint();
    }

    @Override
    public Endpoint outboundEndpoint() {
        return configuration.outboundEndpoint();
    }
}
