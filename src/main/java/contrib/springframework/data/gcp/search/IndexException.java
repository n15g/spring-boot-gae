package contrib.springframework.data.gcp.search;

/**
 * An exception has occurred when saving or retrieving from a search index.
 */
public class IndexException extends RuntimeException {

    /**
     * Create a new instance.
     *
     * @param message Exception message.
     */
    public IndexException(String message) {
        super(message);
    }

    /**
     * Create a new instance.
     *
     * @param cause Root cause.
     */
    public IndexException(Throwable cause) {
        super(cause);
    }
}
