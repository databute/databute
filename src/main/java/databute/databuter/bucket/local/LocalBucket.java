package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import databute.databuter.Databuter;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entity.*;
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

    private final Map<EntityKey, Entity> entities;
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
    public void get(EntityKey entityKey, EntityCallback callback) {
        try {
            final Entity entity = entities.get(entityKey);
            if (entity == null) {
                callback.onFailure(new NotFoundException(entityKey.key()));
            } else {
                callback.onSuccess(entity);
            }
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    @Override
    public void add(Entity entity, EntityCallback callback) {
        try {
            checkNotNull(entity, "entity");

            final EntityKey entityKey = entity.key();
            if (entities.containsKey(entityKey)) {
                callback.onFailure(new DuplicateEntityKeyException(entityKey.key()));
            } else {
                final boolean added = (entities.putIfAbsent(entityKey, entity) == null);
                if (added) {
                    callback.onSuccess(entity);
                } else {
                    callback.onFailure(new DuplicateEntityKeyException(entityKey.key()));
                }
            }
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    @Override
    public void remove(EntityKey entityKey, EntityCallback callback) {
        try {
            final Entity entity = entities.remove(entityKey);
            if (entity == null) {
                callback.onFailure(new NotFoundException(entityKey.key()));
            } else {
                callback.onSuccess(entity);
            }
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

                final Entity expiredEntity = entities.remove(expiration.key());
                logger.debug("Expired entity {}.", expiredEntity.key());

                count += 1;
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
