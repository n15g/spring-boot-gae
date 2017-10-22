package contrib.springframework.data.gcp.search.query;

import com.googlecode.objectify.Result;

import javax.annotation.Nonnull;

/**
 * Executes... {@link Query queries}.
 *
 * @param <E> Entity type.
 */
@FunctionalInterface
public interface QueryExecutor<E> {

    /**
     * Execute the given query, returning the result.
     *
     * @param query The query to execute.
     * @return Result of executed query.
     */
    @Nonnull
    Result<E> execute(Query query);
}
