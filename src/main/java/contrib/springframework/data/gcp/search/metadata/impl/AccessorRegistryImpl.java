package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchId;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.Accessor;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link AccessorRegistry} implementation.
 */
public class AccessorRegistryImpl implements AccessorRegistry {

    private final Map<Class<?>, Map<String, Accessor>> membersByFieldName = new HashMap<>();
    private final Map<Class<?>, Map<String, Accessor>> membersByEncodedName = new HashMap<>();
    private final Map<Class<?>, Accessor> idFieldCache = new HashMap<>();
    private final IndexTypeRegistry indexTypeRegistry;

    /**
     * Create a new instance.
     *
     * @param indexTypeRegistry Registry to use for determining field index types.
     */
    public AccessorRegistryImpl(IndexTypeRegistry indexTypeRegistry) {
        this.indexTypeRegistry = indexTypeRegistry;
    }

    @Override
    public AccessorRegistry register(Class<?> entityClass) {
        membersByFieldName.put(entityClass, analyze(entityClass));
        return this;
    }

    @Override
    public Accessor get(Class<?> entityClass, String memberName) {
        Accessor accessor = get(entityClass).get(memberName);
        if (accessor == null) {
            throw new IllegalArgumentException(String.format("'%s' is not an indexed member on entity class: %s", memberName, entityClass));
        }
        return accessor;
    }

    @Override
    public Accessor getByEncodedName(Class<?> entityClass, String encodedName) {
        if (!membersByEncodedName.containsKey(entityClass)) {
            register(entityClass);
        }

        Accessor accessor = membersByEncodedName.get(entityClass).get(encodedName);

        if (accessor == null) {
            throw new IllegalArgumentException(String.format("Encoded name '%s' is not an indexed member on entity class: %s", encodedName, entityClass));
        }

        return accessor;
    }

    @Override
    public Map<String, Accessor> get(Class<?> entityClass) {
        return membersByFieldName.computeIfAbsent(entityClass, this::analyze);
    }

    @Override
    public Accessor getIdAccessor(Class<?> entityClass) {
        if (!idFieldCache.containsKey(entityClass)) {
            register(entityClass);
        }

        Accessor accessor = idFieldCache.get(entityClass);

        if (accessor == null) {
            throw new IllegalArgumentException(String.format("No @SearchId on entity class: %s", entityClass));
        }

        return accessor;
    }

    protected Map<String, Accessor> analyze(Class<?> entityClass) {
        Map<String, Accessor> result = new HashMap<>();

        result.putAll(registerFields(entityClass));
        result.putAll(registerMethods(entityClass));

        membersByEncodedName.put(entityClass,
                result.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getValue().getEncodedName(),
                                Map.Entry::getValue
                        ))
        );

        return result;
    }


    protected Map<String, Accessor> registerFields(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter((field) -> field.isAnnotationPresent(SearchIndex.class) || field.isAnnotationPresent(SearchId.class))
                .map((field) -> {
                    if (field.isAnnotationPresent(SearchId.class)) {
                        idFieldCache.put(entityClass, new FieldAccessor(entityClass, field, IndexType.IDENTIFIER));
                    }

                    return new FieldAccessor(entityClass, field, indexTypeRegistry);
                }).collect(Collectors.toMap(Accessor::getMemberName, accessor -> accessor));
    }

    protected Map<String, Accessor> registerMethods(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredMethods())
                .filter((field) -> field.isAnnotationPresent(SearchIndex.class) || field.isAnnotationPresent(SearchId.class))
                .map((method) -> {
                    if (method.isAnnotationPresent(SearchId.class)) {
                        idFieldCache.put(entityClass, new MethodAccessor(entityClass, method, IndexType.IDENTIFIER));
                    }

                    return new MethodAccessor(entityClass, method, indexTypeRegistry);
                }).collect(Collectors.toMap(Accessor::getMemberName, accessor -> accessor));
    }
}
