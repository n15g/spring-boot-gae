package contrib.springframework.data.gcp.search.query;

import com.google.appengine.api.search.QueryOptions;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;

import java.util.function.Function;

/**
 * Compile a Google Search API {@link QueryOptions} object from a {@link Query} object.
 */
public class QueryOptionsCompiler implements Function<Query<?>, QueryOptions> {
    final SortOptionsCompiler sortOptionsCompiler;

    /**
     * Create a new instance.
     *
     * @param searchMetadata Search metadata.
     */
    public QueryOptionsCompiler(SearchMetadata searchMetadata) {
        sortOptionsCompiler = new SortOptionsCompiler(searchMetadata);
    }

    @Override
    public QueryOptions apply(Query<?> query) {
        final QueryOptions.Builder options = QueryOptions.newBuilder();

        int offset = query.getSkip().orElse(0);
        options.setOffset(offset);

        query.getLimit().ifPresent(limit -> options.setLimit(limit + offset));
        query.getAccuracy().ifPresent(options::setNumberFoundAccuracy);
        options.setSortOptions(sortOptionsCompiler.apply(query));

        options.setReturningIdsOnly(query.isIdsOnly());

        return options.build();
    }
}
