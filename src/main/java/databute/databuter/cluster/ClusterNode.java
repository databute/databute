package databute.databuter.cluster;

public interface ClusterNode {

    String id();

    ClusterNodeConfiguration configuration();

    String address();

    int port();

}
