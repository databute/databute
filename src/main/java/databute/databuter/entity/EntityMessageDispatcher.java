package databute.databuter.entity;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityMessageDispatcher {

    private final Map<String, EntityCallback> callbacks;

    public EntityMessageDispatcher() {
        this.callbacks = Maps.newConcurrentMap();
    }

    public void enqueue(String id, EntityCallback callback) {
        checkNotNull(id, "id");
        checkNotNull(callback, "callback");

        callbacks.put(id, callback);
    }

    public EntityCallback dequeue(String id) {
        return callbacks.remove(id);
    }
}
