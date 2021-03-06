package databute.databuter.entry;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntryMessageDispatcher {

    private final Map<String, EntryCallback> callbacks;

    public EntryMessageDispatcher() {
        this.callbacks = Maps.newConcurrentMap();
    }

    public void enqueue(String id, EntryCallback callback) {
        checkNotNull(id, "id");
        checkNotNull(callback, "callback");

        callbacks.put(id, callback);
    }

    public EntryCallback dequeue(String id) {
        return callbacks.remove(id);
    }
}
