package contrib.springframework.data.gcp.search.query;

import com.googlecode.objectify.Result;
import contrib.springframework.data.gcp.search.Operator;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@link QueryBuilder} implementation.
 *
 * @param <E> Entity type.
 */
public class QueryImpl<E> implements QueryBuilder<E>, Query<E> {

    private final Class<E> resultType;
    private final QueryExecutor<E> queryExecutor;
    private final List<Query.Fragment> fragments = new ArrayList<>();
    private Sort sort = null;
    private Integer limit = null;
    private Integer skip = null;
    private Integer accuracy = null;

    /**
     * Create a new instance.
     *
     * @param resultType    The type of result this query produces.
     * @param queryExecutor The executor to use to execute this query.
     */
    public QueryImpl(Class<E> resultType, QueryExecutor<E> queryExecutor) {
        this.resultType = resultType;
        this.queryExecutor = queryExecutor;
    }

    @Nonnull
    @Override
    public Class<E> getResultType() {
        return resultType;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> query(CharSequence query) {
        fragments.add(
                new RawQueryFragment(query.toString())
        );
        return this;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> filter(String field, Operator operator, @Nullable Object value) {
        fragments.add(
                new PredicateQueryFragment(field, operator, value)
        );
        return this;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> skip(int skip) {
        this.skip = skip;
        return this;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> accuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    @Nonnull
    @Override
    public QueryBuilder<E> order(String field, Sort.Direction direction) {
        Sort newSort = new Sort(new Sort.Order(direction, field));
        this.sort = this.sort != null ? this.sort.and(newSort) : newSort;

        return this;
    }

    @Nonnull
    @Override
    public Query<E> build() {
        return this;
    }

    @Nonnull
    @Override
    public Result<E> execute() {
        return queryExecutor.execute(this);
    }

    @Nonnull
    @Override
    public List<Fragment> getFragments() {
        return fragments;
    }

    @Nonnull
    @Override
    public Optional<Integer> getLimit() {
        return Optional.ofNullable(limit);
    }

    @Nonnull
    @Override
    public Optional<Integer> getSkip() {
        return Optional.ofNullable(skip);
    }

    @Nonnull
    @Override
    public Optional<Integer> getAccuracy() {
        return Optional.ofNullable(accuracy);
    }

    @Nonnull
    @Override
    public Optional<Sort> getSort() {
        return Optional.ofNullable(sort);
    }

}
