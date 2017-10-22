package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interface for classes that manage an entity.
 *
 * @param <E> Class of entity that is managed.
 * @param <I> Managed entity's id class.
 */
public interface EntityManager<E, I> extends ObjectifyAware {
    /**
     * @return The type of entity this repository handles.
     */
    @Nonnull
    Class<E> getEntityType();

    /**
     * @return The id type of the entity this repository handles.
     */
    @Nonnull
    Class<I> getIdType();

    /**
     * Return the id field for the managed entity type.
     *
     * @return Managed entity id field.
     */
    @Nonnull
    default Field getIdField() {
        Class<E> entityType = getEntityType();

        String idFieldName = ofy().factory().getMetadata(entityType).getKeyMetadata().getIdFieldName();
        return FieldUtils.getField(entityType, idFieldName, true);
    }

    /**
     * @return Key class for the managed entity.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default Class<Key<E>> getKeyClass() {
        return (Class) Key.class;
    }

    /**
     * Get a key for the given entity.
     *
     * @param entity The entity.
     * @return Entity key.
     */
    @Nonnull
    default Key<E> getKey(E entity) {
        return Key.create(entity);
    }

    /**
     * Get keys for a list of entities.
     *
     * @param entities The entities.
     * @return List of keys.
     */
    @Nonnull
    default List<Key<E>> getKey(Collection<E> entities) {
        return entities.stream()
                .map(this::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Get keys for a list of entities.
     *
     * @param entities The entities.
     * @return List of keys.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<Key<E>> getKey(E... entities) {
        return getKey(Arrays.asList(entities));
    }

    /**
     * Create a map of entities keyed by their entity key.
     *
     * @param entities Entities to map.
     * @return Entity map.
     */
    @Nonnull
    default Map<Key<E>, E> toKeyMap(Collection<E> entities) {
        return entities.stream().collect(Collectors.toMap(this::getKey, entity -> entity));
    }

    /**
     * Get the id of an entity.
     *
     * @param entity The entity to get the id of.
     * @return Entity id.
     */
    @SuppressWarnings("unchecked")
    default I getId(E entity) {
        try {
            return (I) getIdField().get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Cannot get id for entity type %s - %s", getEntityType().getSimpleName(), entity), e);
        }
    }

    /**
     * Get the ids of a collection of entities.
     *
     * @param entities The entities.
     * @return List of ids.
     */
    @Nonnull
    default List<I> getId(Collection<E> entities) {
        return entities.stream().map(this::getId).collect(Collectors.toList());
    }

    /**
     * Get the ids of a collection of entities.
     *
     * @param entities The entities.
     * @return List of ids.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default List<I> getId(E... entities) {
        return getId(Arrays.asList(entities));
    }

    /**
     * Return whether the given entity has an id set.
     *
     * @param entity The entity to check.
     * @return Whether the entity has an id.
     */
    default boolean hasId(E entity) {
        return getId(entity) != null;
    }

    /**
     * Return whether the given entity has no id set.
     *
     * @param entity The entity to check.
     * @return Whether the entity has no id.
     */
    default boolean hasNoId(E entity) {
        return !hasId(entity);
    }
}
