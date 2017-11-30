package org.springframework.contrib.gae.search;

import com.google.appengine.api.search.ScoredDocument;
import org.springframework.contrib.gae.search.query.Query;
import org.springframework.contrib.gae.search.query.QueryBuilder;
import org.springframework.contrib.gae.search.query.Result;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

import static com.google.common.util.concurrent.Runnables.doNothing;

/**
 * A no-operation {@link SearchService} implementation.
 */
public class NoOpSearchService implements SearchService {

    private static final String NOPE = "Search is not currently supported, please register an operational SearchService for this entity type.";

    @Nonnull
    @Override
    public <E> QueryBuilder<E> createQuery(Class<E> entityClass) {
        throw new java.lang.UnsupportedOperationException(NOPE);
    }

    @Override
    public Result<ScoredDocument> execute(Query<?> query) {
        throw new java.lang.UnsupportedOperationException(NOPE);
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
