package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entry.*;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalBucket extends Bucket {

    private static final Logger logger = LoggerFactory.getLogger(LocalBucket.class);

    private final Map<EntryKey, Entry> entities;
    private final PriorityQueue<Expiration> expireQueue;
    private final ScheduledExecutorService expireScheduler;
    private final ReentrantLock expireLock;

    public LocalBucket(BucketConfiguration configuration) {
        super(configuration);
        this.entities = Maps.newHashMap();
        this.expireQueue = Queues.newPriorityQueue();
        this.expireScheduler = Executors.newSingleThreadScheduledExecutor();
        this.expireLock = new ReentrantLock();

        final long period = Databuter.instance().configuration().expireSchedulePeriod();
        final long initialPeriod = (long) (period * RandomUtils.nextDouble(0.0, 2.0));
        this.expireScheduler.scheduleAtFixedRate(this::doExpire, initialPeriod, period, TimeUnit.SECONDS);
    }

    @Override
    public void get(EntryKey entryKey, EntryCallback callback) {
        try {
            final Entry entry = entities.get(entryKey);
            if (entry == null) {
                callback.onFailure(new NotFoundException(entryKey.key()));
            } else {
                if (entry.willBeExpire() && entry.expirationTimestamp().isBefore(Instant.now())) {
                    entities.remove(entryKey);

                    callback.onFailure(new NotFoundException(entryKey.key()));
                } else {
                    callback.onSuccess(entry);
                }
            }
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    @Override
    public void add(Entry entry, EntryCallback callback) {
        try {
            checkNotNull(entry, "entry");

            final EntryKey entryKey = entry.key();
            if (entities.containsKey(entryKey)) {
                callback.onFailure(new DuplicateEntryKeyException(entryKey.key()));
            } else {
                final boolean added = (entities.putIfAbsent(entryKey, entry) == null);
                if (added) {
                    callback.onSuccess(entry);
                } else {
                    callback.onFailure(new DuplicateEntryKeyException(entryKey.key()));
                }
            }
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    @Override
    public void remove(EntryKey entryKey, EntryCallback callback) {
        try {
            final Entry entry = entities.remove(entryKey);
            if (entry == null) {
                callback.onFailure(new NotFoundException(entryKey.key()));
            } else {
                callback.onSuccess(entry);
            }
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    @Override
    public void expire(EntryKey entryKey, Instant expirationTimestamp, EntryCallback callback) {
        try {
            final Entry entry = entities.get(entryKey);

            if (entry == null) {
                callback.onFailure(new NotFoundException(entryKey.key()));
                return;
            }

            if (expirationTimestamp == null) {
                // 엔티티에 할당된 TTL을 제거하여 삭제되지 않도록 함.
                expireLock.lock();
                try {
                    entry.cancelExpiration();
                } finally {
                    expireLock.unlock();
                }
            } else {
                expireLock.lock();
                try {
                    expireQueue.add(new Expiration(entryKey, expirationTimestamp));
                    entry.expireAt(expirationTimestamp);
                } finally {
                    expireLock.unlock();
                }
            }

            callback.onSuccess(entry);
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    private void doExpire() {
        expireLock.lock();
        try {
            int count = 0;

            final Iterator<Expiration> expireIterator = expireQueue.iterator();
            while (expireIterator.hasNext()) {
                final Expiration expiration = expireIterator.next();
                if (Instant.now().isBefore(expiration.expireTimestamp())) {
                    break;
                }
                expireIterator.remove();

                final Entry entry = entities.get(expiration.key());
                if (entry != null) {
                    if (entry.willBeExpire() && entry.expirationTimestamp().isBefore(Instant.now())) {
                        entities.remove(expiration.key());
                        logger.debug("Expired entry {}.", expiration.key());

                        count += 1;
                    }
                }
            }

            logger.debug("Expired {} entities from bucket {}. {} expirations left.", count, id(), expireQueue.size());
        } finally {
            expireLock.unlock();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
