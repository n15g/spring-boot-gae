package contrib.springframework.data.gcp.search.query;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import javax.annotation.Nonnull;

/**
 * Executes... {@link Query queries}.
 */
@FunctionalInterface
public interface QueryExecutor {

    /**
     * Execute the given filter, returning the result.
     *
     * @param query The filter to executeQuery.
     * @return Result of executed filter.
     */
    @Nonnull
    Results<ScoredDocument> executeQuery(Query<?> query);
}
