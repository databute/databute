package databute.databutee.entry;

public class DuplicateEntryKeyException extends Exception {

    public DuplicateEntryKeyException(String key) {
        super("Found duplicated entry key " + key);
    }
}
