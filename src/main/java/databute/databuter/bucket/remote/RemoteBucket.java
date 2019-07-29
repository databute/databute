package databute.databuter.bucket.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityCallback;
import databute.databuter.entity.EntityKey;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;

public class RemoteBucket extends Bucket {

    public RemoteBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void get(EntityKey entityKey, EntityCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void add(Entity entity, EntityCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(EntityKey entityKey, EntityCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void expire(EntityKey entityKey, Instant expirationTimestamp, EntityCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id())
                .add("configuration", configuration())
                .toString();
    }
}
