package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.search.SearchService;
import gae.spring.data.objectify.search.NoOpSearchService;

import javax.annotation.Nonnull;

/**
 * Basic {@link SearchRepository} implementation.
 * Constructs and caches the {@link SearchService} used to perform searching.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class AbstractSearchRepository<E, I> extends AbstractRepository<E, I> implements SearchRepository<E, I> {

    private SearchService<E> searchService = new NoOpSearchService<>();

    @Nonnull
    @Override
    public SearchService<E> getSearchService() {
        return searchService;
    }
}
