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
    public <E> String getId(E entity) {
        return null;
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(E entity) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(E entity, String id) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(Collection<E> entities) {
        return doNothing();
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(Map<String, E> entities) {
        return doNothing();
    }

    @Override
    public <E> void unindex(Class<E> entityClass, String id) {
        //no-op
    }

    @Override
    public <E> void unindex(Class<E> entityClass, Collection<String> ids) {
        //no-op
    }

    @Override
    public <E> int clear(Class<E> entityClass) {
        return 0;
    }
}
