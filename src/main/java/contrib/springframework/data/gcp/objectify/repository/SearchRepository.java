package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.SearchService;
import contrib.springframework.data.gcp.search.query.QueryBuilder;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * A searchable repository.
 * Extends the functionality of {@link Repository} and {@link AsyncRepository}, indexing the managed entity
 * and providing a mechanism to search on {@link SearchIndex} annotated fields.
 *
 * @param <E> Entity type.
 * @param <I> Entity Id Type.
 */
@NoRepositoryBean
public interface SearchRepository<E, I extends Serializable> extends Repository<E, I> {

    /**
     * @return Search service.
     */
    @Nonnull
    SearchService getSearchService();

    /**
     * Begin a search query.
     *
     * @return Query builder.
     */
    @Nonnull
    default QueryBuilder<E> search() {
        return getSearchService().search(getEntityType());
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
        return getSearchService().index(getKey(entity), entity);
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
        return getSearchService().index(toKeyMap(batch));
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
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable unIndex(E entity) {
        return unindexByKey(getKey(entity));
    }

    /**
     * Remove the given entities from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entities The entities to remove.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable unindex(Collection<E> entities) {
        return unindexByKey(getKey(entities));
    }

    /**
     * Remove the given entities from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param entities The entities to remove.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Runnable unindex(E... entities) {
        return unindex(Arrays.asList(entities));
    }

    /**
     * Remove the entity with the given key from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param key Key of the entity to remove.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable unindexByKey(Key<E> key) {
        return getSearchService().unindex(getEntityType(), key.getName());
    }

    /**
     * Remove entities with the given keys from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param keys Keys of the entities to remove.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    default Runnable unindexByKey(Collection<Key<E>> keys) {
        return getSearchService().unindex(getEntityType(), keys.stream().map(Key::getName));
    }

    /**
     * Remove entities with the given keys from search indexes.
     * If the search service is not configured, no operation will be performed.
     *
     * @param keys Keys of the entities to remove.
     * @return Runnable that can be used to synchronously complete the indexing operation.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Runnable unIndexByKey(Key<E>... keys) {
        return unindexByKey(Arrays.asList(keys));
    }

    /*--------- AsyncRepository ---------*/

    @Nonnull
    @Override
    default Supplier<E> saveAsync(final E entity) {
        boolean needsId = hasNoId(entity);

        final Supplier<E> saveOperation = Repository.super.saveAsync(entity);

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

        final Supplier<List<E>> saveOperation = Repository.super.saveAsync(entities);

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

    @Nonnull
    @Override
    default Runnable deleteAsync(E entity) {
        final Runnable deleteOperation = Repository.super.deleteAsync(entity);
        final Runnable indexOperation = unIndex(entity);

        return () -> {
            indexOperation.run();
            deleteOperation.run();
        };
    }

    @Nonnull
    @Override
    default Runnable deleteAsync(Collection<E> entities) {
        final Runnable deleteOperation = Repository.super.deleteAsync(entities);
        final Runnable indexOperation = unindex(entities);

        return () -> {
            indexOperation.run();
            deleteOperation.run();
        };
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
        Runnable deleteOperation = Repository.super.deleteByKeyAsync(key);
        Runnable indexOperation = unindexByKey(key);

        return () -> {
            indexOperation.run();
            deleteOperation.run();
        };
    }

    @Nonnull
    @Override
    default Runnable deleteByKeyAsync(Collection<Key<E>> keys) {
        Runnable deleteOperation = Repository.super.deleteByKeyAsync(keys);
        Runnable indexOperation = unindexByKey(keys);

        return () -> {
            indexOperation.run();
            deleteOperation.run();
        };
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    default Runnable deleteByKeyAsync(Key<E>... keys) {
        return deleteByKeyAsync(Arrays.asList(keys));
    }

    /*--------- Repository ---------*/

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
