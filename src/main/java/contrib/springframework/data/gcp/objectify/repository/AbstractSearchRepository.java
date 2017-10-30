package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.search.SearchService;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Basic {@link SearchRepository} implementation.
 * Constructs and caches the {@link SearchService} used to perform searching.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class AbstractSearchRepository<E, I extends Serializable> extends AbstractRepository<E, I> implements SearchRepository<E, I> {

    private final SearchService searchService;

    protected AbstractSearchRepository(SearchService searchService) {
        this.searchService = searchService;
    }

    @Nonnull
    @Override
    public SearchService getSearchService() {
        return searchService;
    }
}
