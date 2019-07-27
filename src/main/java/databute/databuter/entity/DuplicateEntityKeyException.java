package databute.databuter.entity;

public class DuplicateEntityKeyException extends Exception {

    public DuplicateEntityKeyException(EntityKey key) {
        super("Found duplicated entity key " + key);
    }
}
