package org.springframework.contrib.gae.search.query;

import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.springframework.core.convert.ConversionService;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Compiles a {@link Query} into a filter string.
 */
public class QueryStringCompiler implements Function<Query<?>, String> {

    private final SearchMetadata searchMetadata;
    private final ConversionService conversionService;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service to use to convert fragment values to filter strings.
     */
    public QueryStringCompiler(SearchMetadata searchMetadata, ConversionService conversionService) {
        this.searchMetadata = searchMetadata;
        this.conversionService = conversionService;
    }

    @Override
    public String apply(Query<?> query) {
        QueryFragmentCompiler compiler = new QueryFragmentCompiler(query.getResultType(), searchMetadata, conversionService);

        return query.getFragments().stream()
                .map(compiler)
                .collect(Collectors.joining(" "));
    }
}
