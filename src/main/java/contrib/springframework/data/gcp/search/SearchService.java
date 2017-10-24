package contrib.springframework.data.gcp.search;

import com.google.common.base.Functions;
import contrib.springframework.data.gcp.search.query.QueryBuilder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface to the GAE full-text search API.
 * Can be used to perform full-text searches against indexed entities.
 */
public interface SearchService {
    /**
     * Begin a search query.
     *
     * @param entityClass For this entity class.
     * @param <E>         Entity type.
     * @return Query builder.
     */
    @Nonnull
    <E> QueryBuilder<E> search(Class<E> entityClass);

    /**
     * Get the id of a search entity.
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     * @return Entity id.
     * @throws IllegalArgumentException If the given entity does not have a {@link SearchId} annotated field.
     */
    <E> String getId(E entity);

    /**
     * Add an entity to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    default <E> Runnable index(E entity) {
        return index(entity, getId(entity));
    }

    /**
     * Add an entity to the search indexes with the specified id.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete
     *
     * @param entity The entity.
     * @param id     The id to store against.
     * @param <E>    Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E> Runnable index(E entity, String id);

    /**
     * Add a collection of entities to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    default <E> Runnable index(Collection<E> entities) {
        return index(
                entities.stream()
                        .collect(Collectors.toMap(
                                this::getId,
                                Functions.identity()
                        ))
        );
    }

    /**
     * Add a collection of entities to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    default <E> Runnable index(Stream<E> entities) {
        return index(entities.collect(Collectors.toList()));
    }

    /**
     * Add a collection of entities to the search indexes.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default <E> Runnable index(E... entities) {
        return index(Arrays.asList(entities));
    }

    /**
     * Add a collection of entities to the search indexes with specified ids.
     * Note: The index operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entities Map of entities keyed by the entity id.
     * @param <E>      Entity type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E> Runnable index(Map<String, E> entities);

    /**
     * Remove an entity from the search indexes by id.
     *
     * @param entityClass Class of entity to unindex.
     * @param id          The entity id.
     */
    <E> void unindex(Class<E> entityClass, String id);

    /**
     * Remove a collection of entities from the search indexes by id.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     */
    <E> void unindex(Class<E> entityClass, Collection<String> ids);

    /**
     * Remove a collection of entities from the search indexes by id.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     */
    default <E> void unindex(Class<E> entityClass, Stream<String> ids) {
        unindex(entityClass, ids.collect(Collectors.toList()));
    }

    /**
     * Remove a collection of entities from the search indexes by id.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     */
    default <E> void unindex(Class<E> entityClass, String... ids) {
        unindex(entityClass, Arrays.asList(ids));
    }

    /**
     * Clear a search index of all entries.
     *
     * @param entityClass Class of entity to clear index for.
     * @return Number of entries removed.
     */
    <E> int clear(Class<E> entityClass);
}
