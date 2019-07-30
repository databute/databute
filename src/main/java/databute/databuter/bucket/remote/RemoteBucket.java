package databute.databuter.bucket.remote;

import com.google.common.base.MoreObjects;
import databute.databuter.bucket.Bucket;
import databute.databuter.bucket.BucketConfiguration;
import databute.databuter.entry.Entry;
import databute.databuter.entry.EntryCallback;
import databute.databuter.entry.EntryKey;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;

public class RemoteBucket extends Bucket {

    public RemoteBucket(BucketConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void get(EntryKey entryKey, EntryCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void add(Entry entry, EntryCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(EntryKey entryKey, EntryCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public void expire(EntryKey entryKey, Instant expirationTimestamp, EntryCallback callback) {
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
