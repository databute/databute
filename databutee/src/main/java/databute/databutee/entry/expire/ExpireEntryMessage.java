package databute.databutee.entry.expire;

import com.google.common.base.MoreObjects;
import databute.databutee.entry.EntryKey;
import databute.databutee.entry.EntryMessage;
import databute.network.message.MessageCode;

import java.time.Instant;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntryMessage implements EntryMessage {

    private final String id;
    private final EntryKey key;
    private final Instant expirationTimestamp;

    public ExpireEntryMessage(EntryKey key, Instant expirationTimestamp) {
        this.id = UUID.randomUUID().toString();
        this.key = checkNotNull(key, "key");
        this.expirationTimestamp = expirationTimestamp;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.EXPIRE_ENTRY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key.key();
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
