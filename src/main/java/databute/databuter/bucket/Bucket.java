package databute.databuter.bucket;

import com.google.common.base.MoreObjects;

import java.util.UUID;

public class Bucket {

    private final String id;

    public Bucket() {
        this.id = UUID.randomUUID().toString();
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
