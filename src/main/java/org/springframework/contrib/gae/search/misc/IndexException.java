package org.springframework.contrib.gae.search.misc;

/**
 * An exception has occurred while performing a search index operation.
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
