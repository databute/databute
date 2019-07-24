package databute.databuter.bucket;

public class BucketException extends Exception {

    public BucketException() {
        super();
    }

    public BucketException(String message) {
        super(message);
    }

    public BucketException(String message, Throwable cause) {
        super(message, cause);
    }

    public BucketException(Throwable cause) {
        super(cause);
    }
}
