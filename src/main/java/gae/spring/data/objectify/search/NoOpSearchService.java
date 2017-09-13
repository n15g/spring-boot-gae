package gae.spring.data.objectify.search;

import com.google.common.util.concurrent.Runnables;
import com.googlecode.objectify.Key;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * A no-operation {@link SearchService} implementation.
 *
 * @param <E> Entity type.
 */
public class NoOpSearchService<E> implements SearchService<E> {
    @Nonnull
    @Override
    public Query<E> search() {
        throw new java.lang.UnsupportedOperationException("Search is not currently supported, please register an operational SearchService for this entity type.");
    }

    @Nonnull
    @Override
    public Runnable index(Key<E> key, E entity) {
        return Runnables.doNothing();
    }

    @Nonnull
    @Override
    public Runnable index(Map<Key<E>, E> entities) {
        return Runnables.doNothing();
    }

    @Nonnull
    @Override
    public Runnable unindex(Key<E> key) {
        return Runnables.doNothing();
    }

    @Nonnull
    @Override
    public Runnable unindex(Collection<Key<E>> keys) {
        return Runnables.doNothing();
    }

    @Override
    public int clear() {
        return 0;
    }
}
