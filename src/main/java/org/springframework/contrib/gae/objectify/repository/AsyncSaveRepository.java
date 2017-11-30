package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Objectify repository for asynchronously saving entities.
 * Each call returns a hook to the asynchronous operation that can be used to complete the operation at a later time. The operation
 * will eventually complete (or fail) regardless of whether the hook is used or not, it simply provides a mechanism for callers to
 * optionally wait for the operation to complete (i.e. treat the operation as synchronous) if required.
 *
 * @param <E> The entity type
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface AsyncSaveRepository<E, I extends Serializable> extends ObjectifyAware, Repository<E, I> {

    /**
     * Put an entity asynchronously asynchronously.
     *
     * @param entity The entity to save.
     * @return Supplier that can be used to return the entity later.
     */
    @Nonnull
    default Supplier<E> saveAsync(final E entity) {
        final Result<Key<E>> saveOperation = ofy().save().entity(entity);
        return () -> {
            saveOperation.now();
            return entity;
        };
    }

    /**
     * Save the given entities asynchronously.
     *
     * @param entities Collection of entities to save.
     * @return Supplier that can be used to return the collection of saved entities later.
     */
    @Nonnull
    default Supplier<List<E>> saveAsync(final Collection<E> entities) {
        final Result<Map<Key<E>, E>> saveOperation = ofy().save().entities(entities);
        return () -> {
            saveOperation.now();
            return new ArrayList<>(entities);
        };
    }

    /**
     * Save the given entities asynchronously.
     *
     * @param entities Collection of entities to save.
     * @return Supplier that can be used to return the collection of saved entities later.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Supplier<List<E>> saveAsync(final E... entities) {
        return saveAsync(Arrays.asList(entities));
    }
}
