package databute.databuter.cluster;

import databute.databuter.network.Endpoint;

public interface ClusterNode {

    String id();

    ClusterNodeConfiguration configuration();

    Endpoint inboundEndpoint();

    Endpoint outboundEndpoint();

}
