package databute.databutee.bucket;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketGroup implements Iterable<Bucket> {

    private final Map<String, Bucket> buckets;

    public BucketGroup() {
        this.buckets = Maps.newConcurrentMap();
    }

    @Override
    public Iterator<Bucket> iterator() {
        return buckets.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super Bucket> action) {
        buckets.values().forEach(action);
    }

    @Override
    public Spliterator<Bucket> spliterator() {
        return buckets.values().spliterator();
    }

    public int count() {
        return buckets.size();
    }

    public Bucket find(String id) {
        return buckets.get(id);
    }

    public Bucket findByKeyFactor(int keyFactor) {
        for (Bucket bucket : buckets.values()) {
            if (bucket.keyFactor() == keyFactor) {
                return bucket;
            }
        }
        return null;
    }

    public boolean add(Bucket bucket) {
        checkNotNull(bucket, "bucket");

        return (buckets.putIfAbsent(bucket.id(), bucket) == null);
    }

    public Bucket remove(String id) {
        checkNotNull(id, "id");

        return buckets.remove(id);
    }
}
