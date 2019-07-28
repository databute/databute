package databute.databuter.entity;

public class NotFoundException extends Exception {

    private final String key;

    public NotFoundException(String key) {
        super("not found key: " + key);
        this.key = key;
    }

    public String key() {
        return key;
    }
}
