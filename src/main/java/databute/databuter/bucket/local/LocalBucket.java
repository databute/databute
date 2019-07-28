package databute.databuter.bucket.local;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entity.*;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalBucket extends Bucket {

    private final Map<EntityKey, Entity> entities;

    public LocalBucket(BucketConfiguration configuration) {
        super(configuration);
        this.entities = Maps.newHashMap();
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
