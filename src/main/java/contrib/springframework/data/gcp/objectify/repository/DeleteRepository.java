package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;

/**
 * Objectify repository for deleting entities.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface DeleteRepository<E, I extends Serializable> extends AsyncDeleteRepository<E, I> {
    /**
     * Delete the given entity.
     *
     * @param entity The entity.
     */
    default void delete(E entity) {
        AsyncDeleteRepository.super.deleteAsync(entity).run();
    }

    /**
     * Delete a set of entities.
     *
     * @param entities The entities.
     */
    default void delete(Collection<E> entities) {
        AsyncDeleteRepository.super.deleteAsync(entities).run();
    }

    /**
     * Delete a set of entities.
     *
     * @param entities The entities.
     */
    @SuppressWarnings("unchecked")
    default void delete(E... entities) {
        AsyncDeleteRepository.super.deleteAsync(entities).run();
    }

    /**
     * Delete the entity with the given key.
     *
     * @param key Key of the entity to delete.
     */
    default void deleteByKey(Key<E> key) {
        AsyncDeleteRepository.super.deleteByKeyAsync(key).run();
    }

    /**
     * Delete the entities with the given keys.
     *
     * @param keys Keys of the entities to delete.
     */
    default void deleteByKey(Collection<Key<E>> keys) {
        AsyncDeleteRepository.super.deleteByKeyAsync(keys).run();
    }

    /**
     * Delete the entities with the given keys.
     *
     * @param keys Keys of the entities to delete.
     */
    @SuppressWarnings("unchecked")
    default void deleteByKey(Key<E>... keys) {
        AsyncDeleteRepository.super.deleteByKeyAsync(keys).run();
    }
}
