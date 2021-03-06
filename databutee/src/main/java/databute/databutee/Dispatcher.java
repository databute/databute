package databute.databutee;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class Dispatcher {

    private final Map<String, Callback> callbacks;

    public Dispatcher() {
        this.callbacks = Maps.newConcurrentMap();
    }

    public void enqueue(String id, Callback callback) {
        checkNotNull(id, "id");
        checkNotNull(callback, "callback");

        callbacks.put(id, callback);
    }

    public Callback dequeue(String id) {
        return callbacks.remove(id);
    }
}
