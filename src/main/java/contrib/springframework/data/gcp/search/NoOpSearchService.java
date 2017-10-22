package contrib.springframework.data.gcp.search;

import contrib.springframework.data.gcp.search.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

import static com.google.common.util.concurrent.Runnables.doNothing;

/**
 * A no-operation {@link SearchService} implementation.
 */
public class NoOpSearchService implements SearchService {
    @Nonnull
    @Override
    public <E> QueryBuilder<E> search(Class<E> entityClass) {
        throw new java.lang.UnsupportedOperationException("Search is not currently supported, please register an operational SearchService for this entity type.");
    }

    @Override
    public <E, I> I getId(E entity) {
        return null;
    }

    @Nonnull
    @Override
    public <E> Runnable index(E entity) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E, I> Runnable index(E entity, I id) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E> Runnable index(Collection<E> entities) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E, I> Runnable index(Map<I, E> entities) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E, I> Runnable unindex(Class<E> entityClass, I id) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E, I> Runnable unindex(Class<E> entityClass, Collection<I> ids) {
        return doNothing();
    }

    @Override
    public <E> int clear(Class<E> entityClass) {
        return 0;
    }
}
