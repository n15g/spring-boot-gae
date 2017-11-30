package org.springframework.contrib.gae.objectify.repository;

import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Objectify Repository.
 * <p>
 * Extends {@link AsyncSaveRepository}, providing synchronous methods to save and load entities using Objectify.
 *
 * @param <E> The entity type.
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface SaveRepository<E, I extends Serializable> extends AsyncSaveRepository<E, I> {
    /**
     * Save the given entity.
     *
     * @param entity The entity.
     * @return the entity.
     */
    @Nonnull
    default E save(final E entity) {
        return AsyncSaveRepository.super.saveAsync(entity).get();
    }

    /**
     * Save the given entities.
     *
     * @param entities The entities.
     * @return the list of saved entities.
     */
    @Nonnull
    default List<E> save(final Collection<E> entities) {
        return AsyncSaveRepository.super.saveAsync(entities).get();
    }

    /**
     * Save the given entities.
     *
     * @param entities The entities.
     * @return the list of saved entities.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<E> save(E... entities) {
        return AsyncSaveRepository.super.saveAsync(entities).get();
    }
}
