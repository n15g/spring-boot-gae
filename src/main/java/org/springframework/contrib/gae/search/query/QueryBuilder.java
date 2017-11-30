package org.springframework.contrib.gae.search.query;

import org.springframework.contrib.gae.search.Operator;
import org.springframework.contrib.gae.search.SearchService;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;

/**
 * Encapsulates the construction of a filter to be performed using the {@link SearchService}.
 *
 * @param <E> Entity type.
 */
public interface QueryBuilder<E> {

    /**
     * Filter by records containing the given value across any field.
     *
     * @param value The value to search for.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> filter(@Nullable Object value);

    /**
     * Filter records.
     *
     * @param field    The field to search.
     * @param operator The operator to apply filtering with.
     * @param value    The value to filterIn by.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> filter(String field, Operator operator, @Nullable Object value);

    /**
     * Filter records matching one of the given values.
     *
     * @param field  The field to filterIn.
     * @param values Collection of values to test against.
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> filterIn(String field, Collection<?> values) {
        return filter(field, Operator.IN, values);
    }

    /**
     * Filter records matching one of the given values.
     *
     * @param field  The field to filterIn.
     * @param values Collection of values to test against.
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> filterIn(String field, Object... values) {
        return filter(field, Operator.IN, Arrays.asList(values));
    }

    /**
     * Limits the number of results returned.
     *
     * @param limit Maximum number of records to return.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> limit(int limit);

    /**
     * Skip the first {@code x} records.
     * Use alongside {@link #limit(int)} to page results.
     *
     * @param offset The number or records to skip.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> skip(int offset);

    /**
     * Allows control of the accuracy of the number of matches on the response. If the number of
     * matches is less than the given accuracy, then it is absolutely correct. Above that, it is
     * an estimate based on samples. A high number has performance implications.
     *
     * @param accuracy The desired accuracy of the response.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> accuracy(int accuracy);

    /**
     * Order results by the given field in ascending order.
     * The sequence in which multiple orders are specified will determine the final {@link Sort} order.
     *
     * @param field The field to order by.
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> order(String field) {
        return order(field, Sort.Direction.ASC);
    }

    /**
     * Order results.
     * The sequence in which multiple orders are specified will determine the final {@link Sort} order.
     *
     * @param field     The field to order by.
     * @param direction The direction to order by.
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> order(String field, Sort.Direction direction);

    /**
     * Set whether the filter will retrieve ids only.
     *
     * @param onlyIds Retrieve ids only?
     * @return Query builder.
     */
    @Nonnull
    QueryBuilder<E> setRetrieveIdsOnly(boolean onlyIds);

    /**
     * Set the filter to retrieve ids only.
     *
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> retrieveIdsOnly() {
        return setRetrieveIdsOnly(true);
    }

    /**
     * Builds the filter and returns the result.
     *
     * @return The compiled {@link Query} object.
     */
    @Nonnull
    Query<E> build();
}
