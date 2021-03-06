package databute.databutee.node;

import com.google.common.base.MoreObjects;
import databute.databutee.network.DatabuterSession;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterNode {

    public static DatabuterNode.Builder builder() {
        return new DatabuterNode.Builder();
    }

    private DatabuterSession session;

    private final String id;
    private final String address;
    private final int port;

    public DatabuterNode(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
    }

    public String id() {
        return id;
    }

    public String address() {
        return address;
    }

    public int port() {
        return port;
    }

    public DatabuterSession session() {
        return session;
    }

    public DatabuterNode session(DatabuterSession session) {
        this.session = checkNotNull(session, "session");
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .add("session", session)
                .toString();
    }

    public static class Builder {

        private String id;
        private String address;
        private int port;

        private Builder() {

        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public DatabuterNode build() {
            return new DatabuterNode(id, address, port);
        }
    }
}
