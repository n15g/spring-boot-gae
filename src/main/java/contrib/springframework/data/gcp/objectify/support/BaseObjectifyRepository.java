package contrib.springframework.data.gcp.objectify.support;

import com.googlecode.objectify.Objectify;
import contrib.springframework.data.gcp.objectify.ObjectifyProxy;
import contrib.springframework.data.gcp.objectify.repository.SearchRepository;
import contrib.springframework.data.gcp.search.NoOpSearchService;
import contrib.springframework.data.gcp.search.SearchService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Basic Objectify repository implementation used to scaffold {@link org.springframework.stereotype.Repository} annotated beans.
 */
public class BaseObjectifyRepository<E, I extends Serializable> implements SearchRepository<E, I> {

    private ObjectifyProxy objectify;
    private SearchService searchService;
    private Class<E> entityType;
    private Class<I> idType;

    /**
     * Create a new instance.
     *
     * @param objectify     Objectify proxy.
     * @param searchService Search service.
     * @param entityType    The type of entity this repository manages.
     * @param idType        The id type of the entity this repository manages.
     */
    public BaseObjectifyRepository(ObjectifyProxy objectify, @Nullable SearchService searchService, Class<E> entityType, Class<I> idType) {
        this.objectify = objectify;
        this.searchService = searchService != null ? searchService : new NoOpSearchService();
        this.entityType = entityType;
        this.idType = idType;
    }

    @Nonnull
    @Override
    public SearchService getSearchService() {
        return searchService;
    }

    @Nonnull
    @Override
    public Class<E> getEntityType() {
        return entityType;
    }

    @Nonnull
    @Override
    public Class<I> getIdType() {
        return idType;
    }

    @Override
    public Objectify ofy() {
        return objectify.ofy();
    }
}
