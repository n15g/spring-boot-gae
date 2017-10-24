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
     * @param <I>    Id type.
     * @return Entity id.
     * @throws IllegalArgumentException If the given entity does not have a {@link SearchId} annotated field.
     */
    <E, I> I getId(E entity);

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
     * @param <I>    Id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E, I> Runnable index(E entity, I id);

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
     * @param <I>      Entity id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E, I> Runnable index(Map<I, E> entities);

    /**
     * Remove an entity from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     * <p>
     *
     * @param entityClass Class of entity to unindex.
     * @param key         The entity key.
     * @param <I>         Entity id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E, I> Runnable unindex(Class<E> entityClass, I key);

    /**
     * Remove a collection of entities from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     * @param <I>         Id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    <E, I> Runnable unindex(Class<E> entityClass, Collection<I> ids);

    /**
     * Remove a collection of entities from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     * @param <I>         Id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    default <E, I> Runnable unindex(Class<E> entityClass, Stream<I> ids) {
        return unindex(entityClass, ids.collect(Collectors.toList()));
    }

    /**
     * Remove a collection of entities from the search indexes by key.
     * Note: The unindex operation is performed asynchronously. The returned {@link Runnable} can be invoked to
     * wait for the operation to complete.
     *
     * @param entityClass Class of entity to unindex.
     * @param ids         The entity ids.
     * @param <E>         Entity type.
     * @param <I>         Id type.
     * @return Index operation completion hook.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default <E, I> Runnable unindex(Class<E> entityClass, I... ids) {
        return unindex(entityClass, Arrays.asList(ids));
    }

    /**
     * Clear a search index of all entries.
     *
     * @param entityClass Class of entity to clear index for.
     * @return Number of entries removed.
     */
    <E> int clear(Class<E> entityClass);
}
