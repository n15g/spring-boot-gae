package contrib.springframework.data.gcp.objectify;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides metadata about Objectify entities.
 */
public interface EntityMetadata {
    /**
     * @param entityClass Entity class.
     * @param <E>         Entity type.
     * @return The id field for the given entity class.
     */
    @Nonnull
    <E> Field getIdField(Class<E> entityClass);

    /**
     * @param entityClass Entity class.
     * @param <E>         Entity type.
     * @return The id type for the given entity class.
     */
    @Nonnull
    <E> Class<?> getIdType(Class<E> entityClass);

    /**
     * Get the id of an entity.
     *
     * @param entity The entity to get the id of.
     * @param <E>    Entity type.
     * @param <I>    Entity id type.
     * @return Entity id.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    default <E, I> I getId(E entity) {
        Class<?> type = entity.getClass();

        try {
            return (I) getIdField(type).get(entity);
        } catch (IllegalAccessException cause) {
            throw new IllegalArgumentException(String.format("Cannot get id for entity %s \nof type %s", entity, type), cause);
        }
    }

    /**
     * Get the ids of a collection of entities.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @param <I>      Entity id type.
     * @return List of ids.
     */
    @Nonnull
    default <E, I> List<I> getId(Collection<E> entities) {
        return entities.stream().<I>map(this::getId).collect(Collectors.toList());
    }

    /**
     * Get the ids of a collection of entities.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @param <I>      Entity id type.
     * @return List of ids.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default <E, I> List<I> getId(E... entities) {
        return getId(Arrays.asList(entities));
    }

    /**
     * Return whether the given entity has an id set.
     *
     * @param entity The entity to check.
     * @param <E>    Entity type.
     * @return Whether the entity has an id.
     */
    default <E> boolean hasId(E entity) {
        return getId(entity) != null;
    }

    /**
     * Return whether the given entity has no id set.
     *
     * @param entity The entity to check.
     * @param <E>    Entity type.
     * @return Whether the entity has no id.
     */
    default <E> boolean hasNoId(E entity) {
        return !hasId(entity);
    }
}
