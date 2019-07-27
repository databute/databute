package databute.databuter.cluster;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import databute.databuter.network.EndpointConfiguration;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClusterConfiguration {

    @JsonProperty("endpoint")
    private EndpointConfiguration endpoint;

    public EndpointConfiguration endpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("endpoint", endpoint)
                .toString();
    }
}
