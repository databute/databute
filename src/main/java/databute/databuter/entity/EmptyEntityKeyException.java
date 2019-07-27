package databute.databuter.entity;

public class EmptyEntityKeyException extends Exception {

    public EmptyEntityKeyException(String key) {
        super("Found empty entity key: " + key);
    }
}
