package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;

/**
 * Exception thrown indicating that an entity can not be loaded with a given key.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * An attempt to load an entity by key has failed.
     *
     * @param key The failed key.
     */
    public EntityNotFoundException(Key<?> key) {
        super(String.format("No entity was found matching the key: %s", key));
    }
}
