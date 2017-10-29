package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchId;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.SearchFieldMetadata;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link SearchFieldMetadataRegistry} implementation.
 */
public class SearchFieldMetadataRegistryImpl implements SearchFieldMetadataRegistry {

    private final Map<Class<?>, Map<String, SearchFieldMetadata>> membersByFieldName = new HashMap<>();
    private final Map<Class<?>, Map<String, SearchFieldMetadata>> membersByEncodedName = new HashMap<>();
    private final Map<Class<?>, SearchFieldMetadata> idFieldCache = new HashMap<>();
    private final IndexTypeRegistry indexTypeRegistry;

    /**
     * Create a new instance.
     *
     * @param indexTypeRegistry Registry to use for determining field index types.
     */
    public SearchFieldMetadataRegistryImpl(IndexTypeRegistry indexTypeRegistry) {
        this.indexTypeRegistry = indexTypeRegistry;
    }

    @Override
    public SearchFieldMetadataRegistry register(Class<?> entityClass) {
        membersByFieldName.put(entityClass, analyze(entityClass));
        return this;
    }

    @Override
    public SearchFieldMetadata get(Class<?> entityClass, String memberName) {
        SearchFieldMetadata searchFieldMetadata = get(entityClass).get(memberName);
        if (searchFieldMetadata == null) {
            throw new IllegalArgumentException(String.format("'%s' is not an indexed member on entity class: %s", memberName, entityClass));
        }
        return searchFieldMetadata;
    }

    @Override
    public SearchFieldMetadata getByEncodedName(Class<?> entityClass, String encodedName) {
        if (!membersByEncodedName.containsKey(entityClass)) {
            register(entityClass);
        }

        SearchFieldMetadata searchFieldMetadata = membersByEncodedName.get(entityClass).get(encodedName);

        if (searchFieldMetadata == null) {
            throw new IllegalArgumentException(String.format("Encoded name '%s' is not an indexed member on entity class: %s", encodedName, entityClass));
        }

        return searchFieldMetadata;
    }

    @Override
    public Map<String, SearchFieldMetadata> get(Class<?> entityClass) {
        return membersByFieldName.computeIfAbsent(entityClass, this::analyze);
    }

    @Override
    public SearchFieldMetadata getIdField(Class<?> entityClass) {
        if (!idFieldCache.containsKey(entityClass)) {
            register(entityClass);
        }

        SearchFieldMetadata searchFieldMetadata = idFieldCache.get(entityClass);

        if (searchFieldMetadata == null) {
            throw new IllegalArgumentException(String.format("No @SearchId on entity class: %s", entityClass));
        }

        return searchFieldMetadata;
    }

    protected Map<String, SearchFieldMetadata> analyze(Class<?> entityClass) {
        Map<String, SearchFieldMetadata> result = new HashMap<>();

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


    protected Map<String, SearchFieldMetadata> registerFields(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter((field) -> field.isAnnotationPresent(SearchIndex.class) || field.isAnnotationPresent(SearchId.class))
                .map((field) -> {
                    if (field.isAnnotationPresent(SearchId.class)) {
                        idFieldCache.put(entityClass, new FieldSearchFieldMetadata(entityClass, field, IndexType.IDENTIFIER));
                    }

                    return new FieldSearchFieldMetadata(entityClass, field, indexTypeRegistry);
                }).collect(Collectors.toMap(SearchFieldMetadata::getMemberName, accessor -> accessor));
    }

    protected Map<String, SearchFieldMetadata> registerMethods(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredMethods())
                .filter((field) -> field.isAnnotationPresent(SearchIndex.class) || field.isAnnotationPresent(SearchId.class))
                .map((method) -> {
                    if (method.isAnnotationPresent(SearchId.class)) {
                        idFieldCache.put(entityClass, new MethodSearchFieldMetadata(entityClass, method, IndexType.IDENTIFIER));
                    }

                    return new MethodSearchFieldMetadata(entityClass, method, indexTypeRegistry);
                }).collect(Collectors.toMap(SearchFieldMetadata::getMemberName, accessor -> accessor));
    }
}
