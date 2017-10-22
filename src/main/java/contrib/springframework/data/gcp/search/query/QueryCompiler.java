package contrib.springframework.data.gcp.search.query;

import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import org.springframework.core.convert.ConversionService;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Compiles a {@link Query} into a query string.
 *
 * @param <E> Entity type.
 */
public class QueryCompiler<E> implements Function<Query<E>, String> {

    private final Function<Query.Fragment, String> fragmentConverter;

    /**
     * Create a new instance.
     *
     * @param entityType        Entity type.
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service to use to convert fragment values to query strings.
     */
    public QueryCompiler(Class<E> entityType, SearchMetadata searchMetadata, ConversionService conversionService) {
        this.fragmentConverter = new QueryFragmentCompiler(entityType, searchMetadata, conversionService);
    }

    @Override
    public String apply(Query<E> query) {
        return query.getFragments().stream()
                .map(fragmentConverter)
                .collect(Collectors.joining(" "));
    }
}
