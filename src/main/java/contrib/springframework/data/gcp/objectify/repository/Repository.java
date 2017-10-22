package contrib.springframework.data.gcp.objectify.repository;

import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Objectify Repository.
 * <p>
 * Extends {@link AsyncRepository}, providing synchronous methods to save and load entities using Objectify.
 *
 * @param <E> The entity type.
 * @param <I> The id type of the entity.
 */
@NoRepositoryBean
public interface Repository<E, I extends Serializable> extends AsyncRepository<E, I> {
    /**
     * Save the given entity.
     *
     * @param entity The entity.
     * @return the entity.
     */
    @Nonnull
    default E save(final E entity) {
        return AsyncRepository.super.saveAsync(entity).get();
    }

    /**
     * Save the given entities.
     *
     * @param entities The entities.
     * @return the list of saved entities.
     */
    @Nonnull
    default List<E> save(final Collection<E> entities) {
        return AsyncRepository.super.saveAsync(entities).get();
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
        return AsyncRepository.super.saveAsync(entities).get();
    }

    /**
     * List all entities.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @return List of entities.
     */
    @Nonnull
    default List<E> findAll() {
        return ofy()
                .load()
                .type(getEntityType())
                .list();
    }

    /**
     * List {@code limit} entities.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param limit Max number of entities to retrieve.
     * @return List of entities.
     */
    @Nonnull
    default List<E> findAll(int limit) {
        return ofy()
                .load()
                .type(getEntityType())
                .limit(limit)
                .list();
    }

    /**
     * Get the entities with the given keys, if they exist.
     *
     * @param keys List of keys to load.
     * @return A map of loaded entities keyed by the entity key.
     */
    @Nonnull
    default Map<Key<E>, Optional<E>> findAll(Collection<Key<E>> keys) {
        return ofy()
                .load()
                .keys(keys)
                .entrySet()
                .parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Optional.ofNullable(entry.getValue())
                ));
    }

    /**
     * Get the entities with the given keys, if they exist.
     *
     * @param keys List of keys to load.
     * @return A map of loaded entities keyed by the entity key.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Map<Key<E>, Optional<E>> findAll(Key<E>... keys) {
        return findAll(Arrays.asList(keys));
    }

    /**
     * Get all entities whose field has the value of the given object.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field Name of the field to filter by.
     * @param value The value to filter by.
     * @return List of entities matching the given value.
     */
    @Nonnull
    default List<E> findAllByField(String field, @Nullable Object value) {
        return ofy()
                .load()
                .type(getEntityType())
                .filter(field, value)
                .list();
    }

    /**
     * Get all entities whose field has the values of any of the given objects.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field  Name of the field to filter by.
     * @param values List of values to filter by.
     * @return List of entities matching the given values.
     */
    @Nonnull
    default List<E> findAllByField(String field, List<?> values) {
        return ofy()
                .load()
                .type(getEntityType())
                .filter(String.format("%s %s", field, Query.FilterOperator.IN.toString()), values)
                .list();
    }

    /**
     * Get all entities whose field has the values of any of the given objects.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field  Name of the field to filter by.
     * @param values List of values to filter by.
     * @return List of entities matching the given values.
     */
    @Nonnull
    default List<E> findAllByField(String field, Object... values) {
        return findAllByField(field, Arrays.asList(values));
    }

    /**
     * Get the entity with the given key.
     *
     * @param key The key.
     * @return The entity or an empty {@link Optional} if none exists.
     */
    @Nonnull
    default Optional<E> findOne(Key<E> key) {
        return Optional.ofNullable(
                ofy()
                        .load()
                        .key(key)
                        .now()
        );
    }

    /**
     * Delete the given entity.
     *
     * @param entity The entity.
     */
    default void delete(E entity) {
        AsyncRepository.super.deleteAsync(entity).run();
    }

    /**
     * Delete a set of entities.
     *
     * @param entities The entities.
     */
    default void delete(Collection<E> entities) {
        AsyncRepository.super.deleteAsync(entities).run();
    }

    /**
     * Delete a set of entities.
     *
     * @param entities The entities.
     */
    @SuppressWarnings("unchecked")
    default void delete(E... entities) {
        AsyncRepository.super.deleteAsync(entities).run();
    }

    /**
     * Delete the entity with the given key.
     *
     * @param key Key of the entity to delete.
     */
    default void deleteByKey(Key<E> key) {
        AsyncRepository.super.deleteByKeyAsync(key).run();
    }

    /**
     * Delete the entities with the given keys.
     *
     * @param keys Keys of the entities to delete.
     */
    default void deleteByKey(Collection<Key<E>> keys) {
        AsyncRepository.super.deleteByKeyAsync(keys).run();
    }

    /**
     * Delete the entities with the given keys.
     *
     * @param keys Keys of the entities to delete.
     */
    @SuppressWarnings("unchecked")
    default void deleteByKey(Key<E>... keys) {
        AsyncRepository.super.deleteByKeyAsync(keys).run();
    }
}
