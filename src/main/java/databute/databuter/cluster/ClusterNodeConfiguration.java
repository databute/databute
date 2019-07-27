package databute.databuter.cluster;

import com.google.common.base.MoreObjects;
import databute.databuter.network.EndpointConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClusterNodeConfiguration {

    public static ClusterNodeConfiguration.Builder builder() {
        return new ClusterNodeConfiguration.Builder();
    }

    private final String id;
    private final EndpointConfiguration inboundEndpoint;
    private final EndpointConfiguration outboundEndpoint;

    public ClusterNodeConfiguration(String id,
                                    EndpointConfiguration inboundEndpoint,
                                    EndpointConfiguration outboundEndpoint) {
        this.id = checkNotNull(id, "id");
        this.inboundEndpoint = checkNotNull(inboundEndpoint, "inboundEndpoint");
        this.outboundEndpoint = checkNotNull(outboundEndpoint, "outboundEndpoint");
    }

    public String id() {
        return id;
    }

    public EndpointConfiguration inboundEndpoint() {
        return inboundEndpoint;
    }

    public EndpointConfiguration outboundEndpoint() {
        return outboundEndpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("inboundEndpoint", inboundEndpoint)
                .add("outboundEndpoint", outboundEndpoint)
                .toString();
    }

    public static class Builder {

        private String id;
        private EndpointConfiguration inboundEndpoint;
        private EndpointConfiguration outboundEndpoint;

        private Builder() {
            super();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder inboundEndpoint(EndpointConfiguration inboundEndpoint) {
            this.inboundEndpoint = inboundEndpoint;
            return this;
        }

        public Builder outboundEndpoint(EndpointConfiguration outboundEndpoint) {
            this.outboundEndpoint = outboundEndpoint;
            return this;
        }

        public ClusterNodeConfiguration build() {
            return new ClusterNodeConfiguration(id, inboundEndpoint, outboundEndpoint);
        }
    }
}
