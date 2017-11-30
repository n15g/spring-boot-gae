package org.springframework.contrib.gae.search.query;

import com.google.appengine.api.search.QueryOptions;
import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import java.util.function.Function;

/**
 * Compiles a {@link Query} into a Google Search API {@link Query}.
 */
public class QueryCompiler implements Function<Query<?>, com.google.appengine.api.search.Query> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCompiler.class);

    private final QueryStringCompiler queryStringCompiler;
    private final QueryOptionsCompiler queryOptionsCompiler;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service.
     */
    public QueryCompiler(SearchMetadata searchMetadata, ConversionService conversionService) {
        queryStringCompiler = new QueryStringCompiler(searchMetadata, conversionService);
        queryOptionsCompiler = new QueryOptionsCompiler(searchMetadata);
    }

    @Override
    public com.google.appengine.api.search.Query apply(Query<?> query) {
        String queryString = queryStringCompiler.apply(query);
        LOGGER.trace("QUERY [{}]", queryString);
        QueryOptions queryOptions = queryOptionsCompiler.apply(query);
        LOGGER.trace(queryOptions.toString());

        return com.google.appengine.api.search.Query.newBuilder()
                .setOptions(queryOptions)
                .build(queryString);
    }
}
