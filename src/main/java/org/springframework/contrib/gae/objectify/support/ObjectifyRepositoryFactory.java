package org.springframework.contrib.gae.objectify.support;

import org.springframework.contrib.gae.objectify.ObjectifyProxy;
import org.springframework.contrib.gae.search.SearchService;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Objectify {@link org.springframework.data.repository.Repository} factory.
 */
public class ObjectifyRepositoryFactory extends RepositoryFactorySupport {

    private final ObjectifyProxy objectify;
    private final SearchService searchService;

    /**
     * Create a new instance.
     *
     * @param objectify     Objectify proxy.
     * @param searchService Search service.
     */
    public ObjectifyRepositoryFactory(@Nonnull ObjectifyProxy objectify, SearchService searchService) {
        Assert.notNull(objectify, String.format("%s must not be null!", ObjectifyProxy.class));

        this.objectify = objectify;
        this.searchService = searchService;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getTargetRepository(RepositoryInformation information) {
        return new AbstractObjectifyRepository(objectify, searchService, information.getDomainType(), information.getIdType());
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return AbstractObjectifyRepository.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> ObjectifyEntityInformation<T, ID> getEntityInformation(Class<T> entityType) {
        return new ObjectifyEntityInformation<>(objectify, entityType);
    }
}
