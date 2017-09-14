package gae.spring.data.objectify.support;

import gae.spring.data.objectify.ObjectifyProxy;
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

    /**
     * Create a new instance.
     *
     * @param objectify Objectify proxy.
     */
    public ObjectifyRepositoryFactory(@Nonnull ObjectifyProxy objectify) {
        Assert.notNull(objectify, String.format("%s must not be null!", ObjectifyProxy.class));

        this.objectify = objectify;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getTargetRepository(RepositoryInformation information) {
        return new BaseObjectifyRepository(objectify, information.getDomainType(), information.getIdType());
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

        return BaseObjectifyRepository.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.support.RepositoryFactorySupport#
     * getEntityInformation(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> ObjectifyEntityInformation<T, ID> getEntityInformation(Class<T> entityType) {
        return new ObjectifyEntityInformation<>(objectify, entityType);
    }
}
