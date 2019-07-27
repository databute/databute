package databute.databuter.cluster;

import databute.databuter.network.EndpointConfiguration;

public interface ClusterNode {

    String id();

    ClusterNodeConfiguration configuration();

    EndpointConfiguration inboundEndpoint();

    EndpointConfiguration outboundEndpoint();

}
