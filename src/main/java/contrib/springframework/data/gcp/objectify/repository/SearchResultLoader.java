package contrib.springframework.data.gcp.objectify.repository;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Load a list of entities from a search query result.
 *
 * @param <E> Entity type.
 */
public class SearchResultLoader<E> implements Function<Results<ScoredDocument>, List<E>> {
    private Function<List<String>, List<E>> entityLookup;

    /**
     * Create a new instance.
     *
     * @param entityLookup Function used to look up entities by web-safe strings.
     */
    public SearchResultLoader(Function<List<String>, List<E>> entityLookup) {
        this.entityLookup = entityLookup;
    }

    @Override
    public List<E> apply(Results<ScoredDocument> results) {
        List<String> webSafeKeys = results.getResults().stream().map(ScoredDocument::getId).collect(Collectors.toList());

        return entityLookup
                .apply(webSafeKeys)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
