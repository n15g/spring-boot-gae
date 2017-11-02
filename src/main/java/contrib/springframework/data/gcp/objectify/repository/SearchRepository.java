package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.SearchService;
import contrib.springframework.data.gcp.search.query.Query;
import contrib.springframework.data.gcp.search.query.QueryBuilder;
import contrib.springframework.data.gcp.search.query.Result;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * A searchable repository.
 * Extends the functionality of {@link SaveRepository} and {@link AsyncSaveRepository}, indexing the managed entity
 * and providing a mechanism to search on {@link SearchIndex} annotated fields.
 *
 * @param <E> Entity type.
 * @param <I> Entity Id Type.
 */
@NoRepositoryBean
public interface SearchRepository<E, I extends Serializable> extends LoadRepository<E, I>, SaveRepository<E, I>, DeleteRepository<E, I> {

    /**
     * @return Search service.
     */
    @Nonnull
    SearchService getSearchService();

    /**
     * Begin a search filter.
     *
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> search() {
        return getSearchService()
                .createQuery(getEntityType())
                .retrieveIdsOnly(); //Only ids are needed by default, we load the entities from these ids. 
    }

    /**
     * Execute a search query.
     *
     * @param query Search query.
     * @return Search result.
     */
    default Result<E> execute(Query<E> query) {
        return getSearchService().execute(query, new SearchResultLoader<>(this::findAllByWebSafeKey));
    }

    /**
     * Create search indexes for an entity.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entity The entity to index.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable index(E entity) {
        return getSearchService().indexAsync(entity, getKey(entity).toWebSafeString());
    }

    /**
     * Create search indexes for a batch of entities.
     * If the search service is not configured, no operation will be performed.
     *
     * @param batch Entities to index.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable index(Collection<E> batch) {
        Map<String, E> toIndex = new HashMap<>();
        toKeyMap(batch).forEach((key, entity) -> toIndex.put(key.toWebSafeString(), entity));

        return getSearchService().indexAsync(toIndex);
    }

    /**
     * Create search indexes for a batch of entities.
     * If the search service is not configured, no operation will be performed.
     *
     * @param batch Entities to index.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Runnable index(E... batch) {
        return index(Arrays.asList(batch));
    }

    /**
     * Remove the given entity from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entity The entity to remove.
     */
    default void unIndex(E entity) {
        unindexByKey(getKey(entity));
    }

    /**
     * Remove the given entities from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entities The entities to remove.
     */
    default void unindex(Collection<E> entities) {
        unindexByKey(getKey(entities));
    }

    /**
     * Remove the given entities from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entities The entities to remove.
     */
    @SuppressWarnings("unchecked")
    default void unindex(E... entities) {
        unindex(Arrays.asList(entities));
    }

    /**
     * Remove the entity with the given key from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param key Key of the entity to remove.
     */
    default void unindexByKey(Key<E> key) {
        getSearchService().unindex(getEntityType(), key.toWebSafeString());
    }

    /**
     * Remove entities with the given keys from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param keys Keys of the entities to remove.
     */
    default void unindexByKey(Collection<Key<E>> keys) {
        getSearchService().unindex(getEntityType(), keys.stream().map(Key::toWebSafeString));
    }

    /**
     * Remove entities with the given keys from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param keys Keys of the entities to remove.
     */
    @SuppressWarnings("unchecked")
    default void unIndexByKey(Key<E>... keys) {
        unindexByKey(Arrays.asList(keys));
    }

    /*--------- AsyncSaveRepository ---------*/

    @Nonnull
    @Override
    default Supplier<E> saveAsync(final E entity) {
        boolean needsId = hasNoId(entity);

        final Supplier<E> saveOperation = SaveRepository.super.saveAsync(entity);

        // if the entity has no id we need the save to complete so we can index by the generated id.
        if (needsId) {
            saveOperation.get();
        }

        final Runnable indexOperation = index(entity);

        return () -> {
            indexOperation.run();

            saveOperation.get();
            return entity;
        };
    }

    @Nonnull
    @Override
    default Supplier<List<E>> saveAsync(final Collection<E> entities) {
        final List<I> ids = getId(entities);

        final Supplier<List<E>> saveOperation = SaveRepository.super.saveAsync(entities);

        // if any entity has no id we need the save to complete so we can index by the generated id.
        if (ids.contains(null)) {
            saveOperation.get();
        }

        final Runnable indexOperation = index(entities);

        return () -> {
            indexOperation.run();

            saveOperation.get();
            return new ArrayList<>(entities);
        };
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    default Supplier<List<E>> saveAsync(final E... entities) {
        return saveAsync(Arrays.asList(entities));
    }

    /*--------- AsyncDeleteRepository ---------*/

    @Nonnull
    @Override
    default Runnable deleteAsync(E entity) {
        unIndex(entity);
        return DeleteRepository.super.deleteAsync(entity);
    }

    @Nonnull
    @Override
    default Runnable deleteAsync(Collection<E> entities) {
        unindex(entities);
        return DeleteRepository.super.deleteAsync(entities);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    default Runnable deleteAsync(E... entities) {
        return deleteAsync(Arrays.asList(entities));
    }

    @Nonnull
    @Override
    default Runnable deleteByKeyAsync(Key<E> key) {
        unindexByKey(key);
        return DeleteRepository.super.deleteByKeyAsync(key);
    }

    @Nonnull
    @Override
    default Runnable deleteByKeyAsync(Collection<Key<E>> keys) {
        unindexByKey(keys);
        return DeleteRepository.super.deleteByKeyAsync(keys);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    default Runnable deleteByKeyAsync(Key<E>... keys) {
        return deleteByKeyAsync(Arrays.asList(keys));
    }

    /*--------- SaveRepository ---------*/

    @Nonnull
    @Override
    default E save(final E entity) {
        return saveAsync(entity).get();
    }

    @Nonnull
    @Override
    default List<E> save(final Collection<E> entities) {
        return saveAsync(entities).get();
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    default List<E> save(E... entities) {
        return saveAsync(entities).get();
    }

    /*--------- DeleteRepository ---------*/


    @Override
    default void delete(E entity) {
        deleteAsync(entity).run();
    }

    @Override
    default void delete(Collection<E> entities) {
        deleteAsync(entities).run();
    }

    @Override
    @SuppressWarnings("unchecked")
    default void delete(E... entities) {
        deleteAsync(entities).run();
    }

    @Override
    default void deleteByKey(Key<E> key) {
        deleteByKeyAsync(key).run();
    }

    @Override
    default void deleteByKey(Collection<Key<E>> keys) {
        deleteByKeyAsync(keys).run();
    }

    @Override
    @SuppressWarnings("unchecked")
    default void deleteByKey(Key<E>... keys) {
        deleteByKeyAsync(keys).run();
    }
}
