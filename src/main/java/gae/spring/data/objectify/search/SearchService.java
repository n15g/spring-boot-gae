package gae.spring.data.objectify.search;

import com.googlecode.objectify.Key;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Interface to the GAE full-text search API.
 * Used to execute arbitrary queries against a given datastore entity.
 *
 * @param <E> Entity type.
 */
public interface SearchService<E> {
    /**
     * Begin a search query.
     *
     * @return Query builder.
     */
    @Nonnull
    Query<E> search();

    /**
     * Add an entity to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete
     *
     * @param key    The entity key.
     * @param entity The entity.
     * @return Index operation completion hook.
     */
    @Nonnull
    Runnable index(Key<E> key, E entity);

    /**
     * Add a collection of entities to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entities Map of entities keyed by the entity key.
     * @return Index operation completion hook.
     */
    @Nonnull
    Runnable index(Map<Key<E>, E> entities);

    /**
     * Remove an entity from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param key The entity key.
     * @return Index operation completion hook.
     */
    @Nonnull
    Runnable unindex(Key<E> key);

    /**
     * Remove a collection of entities from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param keys The entity keys.
     * @return Index operation completion hook.
     */
    @Nonnull
    Runnable unindex(Collection<Key<E>> keys);

    /**
     * Clear the search indexes of all entries.
     *
     * @return Number of entries removed.
     */
    int clear();
}
