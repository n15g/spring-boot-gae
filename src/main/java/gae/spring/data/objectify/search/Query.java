package gae.spring.data.objectify.search;

import com.googlecode.objectify.Result;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Encapsulates the construction of a query to be performed using the {@link SearchService}.
 *
 * @param <E> Entity type.
 */
public interface Query<E> {

    /**
     * Filter records that have given value across any indexed fields.
     *
     * @param value The value to search for.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> filter(@Nullable CharSequence value);

    /**
     * Filter records.
     *
     * @param field    The field to search.
     * @param operator The operator to apply filtering with.
     * @param value    The value to filter by.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> filter(String field, Operator operator, @Nullable Object value);

    /**
     * Filter records matching one of the given values.
     *
     * @param field  The field to filter.
     * @param values Collection of values to test against.
     * @return The query under construction.
     */
    @Nonnull
    <V> Query<E> filter(String field, Collection<V> values);

    /**
     * Limits the number of results returned.
     *
     * @param limit Maximum number of records to return.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> limit(int limit);

    /**
     * Skip the first {@code x} records.
     * Use alongside {@link #limit(int)} to page results.
     *
     * @param offset The number or records to skip.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> skip(int offset);

    /**
     * Allows control of the accuracy of the number of matches on the response. If the number of
     * matches is less than the given accuracy, then it is absolutely correct. Above that, it is
     * an estimate based on samples. A high number has performance implications.
     *
     * @param accuracy The desired accuracy of the response.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> accuracy(int accuracy);

    /**
     * Order results.
     * The sequence in which multiple orders are specified will determine the final {@link Sort} order.
     *
     * @param field     The field to order by.
     * @param direction The direction to order by.
     * @return The query under construction.
     */
    @Nonnull
    Query<E> order(String field, Sort.Direction direction);

    /**
     * Execute the query and return the {@link Result}.
     *
     * @return Search result.
     */
    @Nonnull
    Result<E> execute();
}
