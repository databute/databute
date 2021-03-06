package databute.databutee.entry;

public class EmptyEntryKeyException extends Exception {

    public EmptyEntryKeyException(String key) {
        super("Found empty entry key: " + key);
    }
}
