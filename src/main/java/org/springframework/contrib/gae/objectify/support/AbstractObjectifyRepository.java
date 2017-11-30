package org.springframework.contrib.gae.objectify.support;

import com.googlecode.objectify.Objectify;
import org.springframework.contrib.gae.objectify.ObjectifyProxy;
import org.springframework.contrib.gae.objectify.repository.ObjectifyRepository;
import org.springframework.contrib.gae.search.NoOpSearchService;
import org.springframework.contrib.gae.search.SearchService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Basic Objectify repository implementation used to scaffold {@link org.springframework.stereotype.Repository} annotated beans.
 */
public class AbstractObjectifyRepository<E, I extends Serializable> implements ObjectifyRepository<E, I> {

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
    public AbstractObjectifyRepository(ObjectifyProxy objectify, @Nullable SearchService searchService, Class<E> entityType, Class<I> idType) {
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
