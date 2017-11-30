package org.springframework.contrib.gae.search;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import org.springframework.contrib.gae.search.query.Query;
import org.springframework.contrib.gae.search.query.QueryBuilder;
import org.springframework.contrib.gae.search.query.Result;
import org.springframework.contrib.gae.search.query.ResultImpl;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface to the GAE full-text search API.
 * Can be used to perform full-text searches against indexed entities.
 */
public interface SearchService {
    /**
     * Begin a search filter.
     *
     * @param entityClass For this entity class.
     * @param <E>         Entity type.
     * @return Query builder.
     */
    @Nonnull
    <E> QueryBuilder<E> createQuery(Class<E> entityClass);

    /**
     * Execute a {@link Query}, returning the result.
     *
     * @param query The query.
     * @return Query result
     */
    Result<ScoredDocument> execute(Query<?> query);

    /**
     * Execute a {@link Query}, returning the result.
     *
     * @param query             The query.
     * @param resultTransformer Transformer used to transform the result.
     * @param <T>               Result type.
     * @return Query result
     */
    default <T> Result<T> execute(Query<?> query, Function<Results<ScoredDocument>, List<T>> resultTransformer) {
        Results<ScoredDocument> rawResult = execute(query).getMetadata();

        return new ResultImpl<>(rawResult, resultTransformer);
    }

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
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     */
    default <E> void index(E entity) {
        indexAsync(entity).run();
    }

    /**
     * Add an entity to the search indexes with the specified id.
     *
     * @param entity The entity.
     * @param id     The id to store against.
     * @param <E>    Entity type.
     */
    default <E> void index(E entity, String id) {
        indexAsync(entity, id).run();
    }

    /**
     * Add a collection of entities to the search indexes with specified ids.
     *
     * @param entities Map of entities keyed by the entity id.
     * @param <E>      Entity type.
     */
    default <E> void index(Map<String, E> entities) {
        indexAsync(entities).run();
    }

    /**
     * Add a collection of entities to the search indexes.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     */
    default <E> void index(Collection<E> entities) {
        indexAsync(entities).run();
    }

    /**
     * Add a collection of entities to the search indexes.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     */
    default <E> void index(Stream<E> entities) {
        indexAsync(entities.collect(Collectors.toList())).run();
    }

    /**
     * Add a collection of entities to the search indexes.
     *
     * @param entities Collection of entities to save.
     * @param <E>      Entity type.
     */
    @SuppressWarnings("unchecked")
    default <E> void index(E... entities) {
        indexAsync(Arrays.asList(entities)).run();
    }

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
    default <E> Runnable indexAsync(E entity) {
        return indexAsync(entity, getId(entity));
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
    <E> Runnable indexAsync(E entity, String id);

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
    default <E> Runnable indexAsync(Collection<E> entities) {
        Map<String, Object> map = new HashMap<>();
        entities.forEach(entity -> map.put(getId(entity), entity));

        return indexAsync(map);
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
    default <E> Runnable indexAsync(Stream<E> entities) {
        return indexAsync(entities.collect(Collectors.toList()));
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
    default <E> Runnable indexAsync(E... entities) {
        return indexAsync(Arrays.asList(entities));
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
    <E> Runnable indexAsync(Map<String, E> entities);

    /**
     * Remove an entity from the search indexes by id.
     *
     * @param entityClass Class of entity to unindex.
     * @param id          The entity id.
     * @param <E>         Entity type.
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
     * @param <E> Entity type.
     * @return Number of entries removed.
     */
    <E> int clear(Class<E> entityClass);
}
