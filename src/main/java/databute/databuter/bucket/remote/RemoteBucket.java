package databute.databuter.bucket.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entity.DuplicateEntityKeyException;
import databute.databuter.entity.Entity;
import databute.databuter.entity.EntityKey;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RemoteBucket extends Bucket {

    public RemoteBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    @Override
    public Entity get(EntityKey entityKey) {
        throw new NotImplementedException();
    }

    @Override
    public Entity add(Entity entity) throws DuplicateEntityKeyException {
        throw new NotImplementedException();
    }

    @Override
    public Entity remove(EntityKey entityKey) {
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
