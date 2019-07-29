package databute.databuter.entity.expire;

import com.google.common.base.MoreObjects;
import databute.databuter.entity.EntityMessage;
import databute.databuter.network.message.MessageCode;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntityMessage implements EntityMessage {

    private final String id;
    private final String key;
    private final Instant expirationTimestamp;

    public ExpireEntityMessage(String id, String key, Instant expirationTimestamp) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.expirationTimestamp = expirationTimestamp;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.EXPIRE_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public Instant expirationTimestamp() {
        return expirationTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("expirationTimestamp", expirationTimestamp)
                .toString();
    }
}
