package gae.spring.data.objectify.support;

import com.googlecode.objectify.Objectify;
import gae.spring.data.objectify.ObjectifyProxy;
import gae.spring.data.objectify.repository.SearchRepository;
import gae.spring.data.objectify.search.NoOpSearchService;
import gae.spring.data.objectify.search.SearchService;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Basic Objectify repository implementation used to scaffold {@link org.springframework.stereotype.Repository} annotated beans.
 */
public class BaseObjectifyRepository<E, I extends Serializable> implements SearchRepository<E, I> {

    private ObjectifyProxy objectify;
    private Class<E> entityType;
    private Class<I> idType;

    /**
     * Create a new instance.
     *
     * @param objectify  Objectify proxy.
     * @param entityType The type of entity this repository manages.
     * @param idType     The id type of the entity this repository manages.
     */
    public BaseObjectifyRepository(ObjectifyProxy objectify, Class<E> entityType, Class<I> idType) {
        this.objectify = objectify;
        this.entityType = entityType;
        this.idType = idType;
    }

    @Nonnull
    @Override
    public SearchService<E> getSearchService() {
        return new NoOpSearchService<>();
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
