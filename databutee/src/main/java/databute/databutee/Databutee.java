package databute.databutee;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import databute.databutee.bucket.Bucket;
import databute.databutee.bucket.BucketGroup;
import databute.databutee.entry.EmptyEntryKeyException;
import databute.databutee.entry.EntryKey;
import databute.databutee.entry.EntryMessage;
import databute.databutee.entry.UnsupportedValueTypeException;
import databute.databutee.entry.delete.DeleteEntryMessage;
import databute.databutee.entry.expire.ExpireEntryMessage;
import databute.databutee.entry.get.GetEntryMessage;
import databute.databutee.entry.set.SetEntryMessage;
import databute.databutee.entry.update.UpdateEntryMessage;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.DatabuterSessionConnector;
import databute.databutee.network.register.RegisterMessage;
import databute.databutee.node.DatabuterNode;
import databute.databutee.node.DatabuterNodeGroup;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private static final Logger logger = LoggerFactory.getLogger(Databutee.class);

    private final DatabuteeConfiguration configuration;
    private final DatabuterNodeGroup databuterNodeGroup;
    private final BucketGroup bucketGroup;
    private final Dispatcher dispatcher;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.databuterNodeGroup = new DatabuterNodeGroup();
        this.bucketGroup = new BucketGroup();
        this.dispatcher = new Dispatcher();
    }

    public DatabuteeConfiguration configuration() {
        return configuration;
    }

    public DatabuterNodeGroup databuterNodeGroup() {
        return databuterNodeGroup;
    }

    public BucketGroup bucketGroup() {
        return bucketGroup;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public void connect() throws ConnectException {
        final List<InetSocketAddress> addresses = Lists.newArrayList(configuration.addresses());
        while (!addresses.isEmpty()) {
            final int index = RandomUtils.nextInt(0, addresses.size());
            final InetSocketAddress address = addresses.remove(index);
            final DatabuterSessionConnector connector = new DatabuterSessionConnector(this, configuration.loopGroup());

            try {
                final DatabuterSession session = connector.connect(address).get();
                logger.info("Connected with server at {}", connector.remoteAddress());

                session.send(new RegisterMessage());
                return;
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to connect to {}.", address);
            }
        }

        throw new ConnectException();
    }

    public void get(String key, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final GetEntryMessage getEntryMessage = new GetEntryMessage(entryKey);
        sendEntryMessage(getEntryMessage, callback);
    }

    public void set(String key, Object value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        if (value instanceof Integer) {
            final Integer integerValue = (Integer) value;
            setInteger(key, integerValue, callback);
        } else if (value instanceof Long) {
            final Long longValue = (Long) value;
            setLong(key, longValue, callback);
        } else if (value instanceof String) {
            final String stringValue = (String) value;
            setString(key, stringValue, callback);
        } else {
            throw new UnsupportedValueTypeException();
        }
    }

    public void setInteger(String key, Integer value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setInteger(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void setLong(String key, Long value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setLong(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void setString(String key, String value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setString(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void setList(String key, List<String> value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setList(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void setSet(String key, Set<String> value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setSet(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void setDictionary(String key, Map<String, String> value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final SetEntryMessage setEntryMessage = SetEntryMessage.setDictionary(entryKey, value);
        sendEntryMessage(setEntryMessage, callback);
    }

    public void update(String key, Object value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        if (value instanceof Integer) {
            final Integer integerValue = (Integer) value;
            updateInteger(key, integerValue, callback);
        } else if (value instanceof Long) {
            final Long longValue = (Long) value;
            updateLong(key, longValue, callback);
        } else if (value instanceof String) {
            final String stringValue = (String) value;
            updateString(key, stringValue, callback);
        } else {
            final String toStringValue = value.toString();
            updateString(key, toStringValue, callback);
        }
    }

    public void updateInteger(String key, Integer value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateInteger(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void updateLong(String key, Long value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateLong(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void updateString(String key, String value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateString(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void updateList(String key, List<String> value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateList(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void updateSet(String key, Set<String> value, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateSet(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void updateDictionary(String key, Map<String, String> value, Callback callback)
            throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final UpdateEntryMessage updateEntryMessage = UpdateEntryMessage.updateDictionary(entryKey, value);
        sendEntryMessage(updateEntryMessage, callback);
    }

    public void delete(String key, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final DeleteEntryMessage deleteEntryMessage = new DeleteEntryMessage(entryKey);
        sendEntryMessage(deleteEntryMessage, callback);
    }

    public void expire(String key, Instant expireAt, Callback callback) throws EmptyEntryKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntryKey entryKey = new EntryKey(key);
        final ExpireEntryMessage expireEntryMessage = new ExpireEntryMessage(entryKey, expireAt);
        sendEntryMessage(expireEntryMessage, callback);
    }

    private void sendEntryMessage(EntryMessage entryMessage, Callback callback) {
        final int count = bucketGroup.count();
        final HashCode hashKey = Hashing.crc32().hashString(entryMessage.key(), StandardCharsets.UTF_8);
        final int keyFactor = Hashing.consistentHash(hashKey, count);
        final Bucket bucket = bucketGroup.findByKeyFactor(keyFactor);
        if (bucket == null) {
            logger.error("Failed to find bucket by keyFactor {}.", keyFactor);
        } else {
            final DatabuterNode activeNode = bucket.activeNode();
            if (activeNode != null) {
                dispatcher.enqueue(entryMessage.id(), callback);
                activeNode.session().send(entryMessage);
                logger.debug("keyFactor: {}, bucket: {}, active node: {}", keyFactor, bucket.id(), activeNode.id());
            } else {
                final DatabuterNode standbyNode = bucket.standbyNode();
                if (standbyNode != null) {
                    dispatcher.enqueue(entryMessage.id(), callback);
                    standbyNode.session().send(entryMessage);
                    logger.debug("keyFactor: {}, bucket: {}, standby node: {}", keyFactor, bucket.id(), standbyNode.id());
                } else {
                    logger.error("Bucket {} does not assigned to any node.", bucket.id());
                }
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .add("databuterNodeGroup", databuterNodeGroup)
                .add("bucketGroup", bucketGroup)
                .toString();
    }
}
