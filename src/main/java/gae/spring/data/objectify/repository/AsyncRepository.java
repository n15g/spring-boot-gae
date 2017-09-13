package gae.spring.data.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Objectify repository which supports asynchronous access to entities.
 * Each call returns a hook to the asynchronous operation that can be used to complete the operation at a later time. The operation
 * will eventually complete (or fail) regardless of whether the hook is used or not, it simply provides a mechanism for callers to
 * optionally wait for the operation to complete (i.e. treat the operation as synchronous) if required.
 *
 * @param <E> The entity type
 * @param <I> The id type of the entity
 */
public interface AsyncRepository<E, I> extends EntityManager<E, I> {

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
    @SuppressWarnings("unchecked")
    @Nonnull
    default Supplier<List<E>> saveAsync(final E... entities) {
        return saveAsync(Arrays.asList(entities));
    }

    /**
     * Delete the given entity asynchronously.
     *
     * @param entity The entity to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @Nonnull
    default Runnable deleteAsync(E entity) {
        final Result<Void> deleteOperation = ofy().delete().entity(entity);
        return deleteOperation::now;
    }

    /**
     * Delete the given entities asynchronously.
     *
     * @param entities The entities to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @Nonnull
    default Runnable deleteAsync(Collection<E> entities) {
        final Result<Void> deleteOperation = ofy().delete().entities(entities);
        return deleteOperation::now;
    }

    /**
     * Delete the given entities asynchronously.
     *
     * @param entities The entities to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Runnable deleteAsync(E... entities) {
        return deleteAsync(Arrays.asList(entities));
    }

    /**
     * Delete the entity with the given key asynchronously.
     *
     * @param key Key of the entity to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @Nonnull
    default Runnable deleteByKeyAsync(Key<E> key) {
        Result<Void> deleteOperation = ofy().delete().key(key);
        return deleteOperation::now;
    }

    /**
     * Delete the entities with the given keys asynchronously.
     *
     * @param keys Keys of the entities to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @Nonnull
    default Runnable deleteByKeyAsync(Collection<Key<E>> keys) {
        Result<Void> deleteOperation = ObjectifyService.ofy().delete().entities(keys);
        return deleteOperation::now;
    }

    /**
     * Delete the entities with the given keys asynchronously.
     *
     * @param keys Keys of the entities to delete.
     * @return Callback that can be used to complete the delete operation later.
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    default Runnable deleteByKeyAsync(Key<E>... keys) {
        return deleteByKeyAsync(Arrays.asList(keys));
    }
}
