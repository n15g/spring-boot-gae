package contrib.springframework.data.gcp.objectify.repository;

import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Objectify repository for loading entities.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface LoadRepository<E, I extends Serializable> extends ObjectifyAware, EntityManager<E, I>, Repository<E, I> {
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
     * @return A list of loaded entities keyed by the entity key.
     */
    @Nonnull
    default List<E> findAll(Collection<Key<E>> keys) {
        return new ArrayList<>(
                ofy()
                    .load()
                    .keys(keys)
                    .values()
        );
    }

    /**
     * Get the entities with the given keys, if they exist.
     *
     * @param keys List of keys to load.
     * @return A list of loaded entities keyed by the entity key.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<E> findAll(Key<E>... keys) {
        return findAll(Arrays.asList(keys));
    }

    /**
     * Get the entities with the given web-safe key strings, if they exist.
     *
     * @param webSafeStrings List of keys to load.
     * @return A list of loaded entities keyed by the web-safe key string.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<E> findAllByWebSafeKey(Collection<String> webSafeStrings) {
        List<Key<E>> keys = webSafeStrings.stream()
                .map(string -> (Key<E>) Key.create(string))
                .collect(Collectors.toList());

        return new ArrayList<>(
                ofy()
                    .load()
                    .keys(keys)
                    .values()
        );
    }

    /**
     * Get the entities with the given web-safe key strings, if they exist.
     *
     * @param webSafeStrings List of keys to load.
     * @return A list of loaded entities keyed by the web-safe key string.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<E> findAllByWebSafeKey(String... webSafeStrings) {
        return findAllByWebSafeKey(Arrays.asList(webSafeStrings));
    }

    /**
     * Get all entities whose field has the value of the given object.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field Name of the field to filterIn by.
     * @param value The value to filterIn by.
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
     * @param field  Name of the field to filterIn by.
     * @param values List of values to filterIn by.
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
     * @param field  Name of the field to filterIn by.
     * @param values List of values to filterIn by.
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
     * Find an entity by its web-safe key string.
     *
     * @param webSafeString Entity string.
     * @return The entity or an empty {@link Optional} if none exists.
     */
    @SuppressWarnings("unchecked")
    default Optional<E> findOneByWebSafeKey(String webSafeString) {
        return Optional.ofNullable(
                ofy()
                        .load()
                        .key((Key<E>) Key.create(webSafeString))
                        .now()
        );
    }
}
