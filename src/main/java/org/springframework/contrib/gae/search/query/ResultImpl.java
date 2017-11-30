package org.springframework.contrib.gae.search.query;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import java.util.List;
import java.util.function.Function;

/**
 * {@link Result} implementation.
 *
 * @param <R> Result type.
 */
public class ResultImpl<R> implements Result<R> {

    private final Results<ScoredDocument> queryResult;
    private final Function<Results<ScoredDocument>, List<R>> resultTransformer;

    /**
     * Create a new instance.
     *
     * @param queryResult       Result of the query.
     * @param resultTransformer Transformer used to transform the raw result into the specified result type.
     */
    public ResultImpl(Results<ScoredDocument> queryResult, Function<Results<ScoredDocument>, List<R>> resultTransformer) {
        this.queryResult = queryResult;
        this.resultTransformer = resultTransformer;
    }

    @Override
    public List<R> getList() {
        return resultTransformer.apply(queryResult);
    }

    @Override
    public Results<ScoredDocument> getMetadata() {
        return queryResult;
    }
}
