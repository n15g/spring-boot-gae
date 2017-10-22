package contrib.springframework.data.gcp.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import contrib.springframework.data.gcp.objectify.config.StaticObjectifyProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods for handling Objectify {@link Key}s and {@link Ref}s.
 */
public class Refs {

    /**
     * Create a {@link Ref} for the given entity.
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     * @return Reference or null if the entity is null.
     */
    @Nullable
    public static <E> Ref<E> ref(@Nullable E entity) {
        return entity == null
                ? null
                : Ref.create(entity);
    }

    /**
     * Create a {@link Ref} for the given entities.
     * Nulls result in nulls.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @return List of references.
     */
    @Nonnull
    public static <E> List<Ref<E>> ref(Collection<E> entities) {
        return entities.stream()
                .map(Refs::ref)
                .collect(Collectors.toList());
    }

    /**
     * Create a {@link Ref} for the given entities.
     * Nulls result in nulls.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @return List of references.
     */
    @SafeVarargs
    public static <E> List<Ref<E>> ref(E... entities) {
        return ref(Arrays.asList(entities));
    }

    /**
     * Create a {@link Ref} for the given key.
     *
     * @param key The key.
     * @param <E> Entity type.
     * @return Reference or null if the entity is null.
     */
    @Nullable
    public static <E> Ref<E> ref(@Nullable Key<E> key) {
        return key == null
                ? null
                : Ref.create(key);
    }

    /**
     * Create a {@link Ref} for the given keys.
     * Nulls result in nulls.
     *
     * @param keys The entity keys.
     * @param <E>  Entity type.
     * @return List of references.
     */
    @Nonnull
    public static <E> List<Ref<E>> refKeys(Collection<Key<E>> keys) {
        return keys.stream()
                .map(Refs::ref)
                .collect(Collectors.toList());
    }

    /**
     * Create a {@link Ref} for the given keys.
     * Nulls result in nulls.
     *
     * @param keys The entity keys.
     * @param <E>  Entity type.
     * @return List of references.
     */
    @Nonnull
    @SafeVarargs
    public static <E> List<Ref<E>> refKeys(Key<E>... keys) {
        return refKeys(Arrays.asList(keys));
    }

    /**
     * Create a key for the given entity.
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     * @return Entity key.
     */
    @Nullable
    public static <E> Key<E> key(@Nullable E entity) {
        return entity != null
                ? Key.create(entity)
                : null;
    }

    /**
     * Create keys for a list of entities.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @return List of keys.
     */
    @Nonnull
    public static <E> List<Key<E>> key(Collection<E> entities) {
        return entities.stream()
                .map(Refs::key)
                .collect(Collectors.toList());
    }

    /**
     * Create keys for a list of entities.
     *
     * @param entities The entities.
     * @param <E>      Entity type.
     * @return List of keys.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <E> List<Key<E>> key(E... entities) {
        return key(Arrays.asList(entities));
    }

    /**
     * Dereference a {@link Ref} by loading the referenced entity.
     *
     * @param ref The {@link Ref}.
     * @param <E> Referenced entity type.
     * @return Dereferenced entity or null if the ref is null.
     */
    @Nullable
    public static <E> E deref(@Nullable Ref<E> ref) {
        return ref != null
                ? ref.get()
                : null;
    }

    /**
     * Dereference a collection of {@link Ref}s by loading the referenced entity.
     *
     * @param refs The {@link Ref}s.
     * @param <E>  Referenced entity type.
     * @return List of dereferenced entities.
     */
    @Nonnull
    public static <E> List<E> deref(Collection<Ref<E>> refs) {
        return refs.stream()
                .map(Refs::deref)
                .collect(Collectors.toList());
    }

    /**
     * Dereference a collection of {@link Ref}s by loading the referenced entity.
     *
     * @param refs The {@link Ref}s.
     * @param <E>  Referenced entity type.
     * @return List of dereferenced entities.
     */
    @Nonnull
    @SafeVarargs
    public static <E> List<E> deref(Ref<E>... refs) {
        return deref(Arrays.asList(refs));
    }

    /**
     * Dereference a {@link Key} by loading the referenced entity.
     *
     * @param key The {@link Key}.
     * @param <E> Referenced entity type.
     * @return Dereferenced entity or null if the key is null.
     */
    @Nullable
    public static <E> E load(@Nullable Key<E> key) {
        return key == null
                ? null
                : StaticObjectifyProxy.get()
                .ofy()
                .load()
                .key(key)
                .now();
    }

    /**
     * Dereference a collection of {@link Key}s by loading the referenced entity.
     *
     * @param keys The {@link Key}s.
     * @param <E>  Referenced entity type.
     * @return List of dereferenced entities.
     */
    @Nonnull
    public static <E> List<E> load(Collection<Key<E>> keys) {
        return keys.stream()
                .map(Refs::load)
                .collect(Collectors.toList());
    }

    /**
     * Dereference a collection of {@link Key}s by loading the referenced entity.
     *
     * @param keys The {@link Key}s.
     * @param <E>  Referenced entity type.
     * @return List of dereferenced entities.
     */
    @Nonnull
    @SafeVarargs
    public static <E> List<E> load(Key<E>... keys) {
        return load(Arrays.asList(keys));
    }
}
