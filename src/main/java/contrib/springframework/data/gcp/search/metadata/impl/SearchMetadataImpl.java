package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.metadata.SearchFieldMetadata;
import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link SearchMetadata} implementation.
 */
public class SearchMetadataImpl implements SearchMetadata {
    private final SearchFieldMetadataRegistry searchFieldMetadataRegistry;
    private final IndexNamingStrategy namingStrategy;

    /**
     * Create a new instance.
     *
     * @param indexTypeRegistry Lookup used to determine index type for a field.
     * @param namingStrategy    The index naming strategy.
     */
    public SearchMetadataImpl(IndexTypeRegistry indexTypeRegistry, IndexNamingStrategy namingStrategy) {
        this.searchFieldMetadataRegistry = new SearchFieldMetadataRegistryImpl(indexTypeRegistry);
        this.namingStrategy = namingStrategy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E, I> I getId(E entity) {
        return (I) searchFieldMetadataRegistry
                .getIdField(entity.getClass())
                .getValue(entity);
    }

    @Override
    public <E> String getIndexName(Class<E> entityClass) {
        return namingStrategy.apply(entityClass);
    }

    @Override
    public Map<String, SearchFieldMetadata> getFields(Class<?> entityClass) {
        return searchFieldMetadataRegistry.get(entityClass);
    }

    @Override
    public SearchFieldMetadata getField(Class<?> entityClass, String memberName) {
        return searchFieldMetadataRegistry.get(entityClass, memberName);
    }

    @Override
    public String encodeFieldName(Class<?> entityType, String fieldName) {
        return searchFieldMetadataRegistry.get(entityType, fieldName).getEncodedName();
    }

    @Override
    public String decodeFieldName(Class<?> entityType, String encodedFieldName) {
        return searchFieldMetadataRegistry.getByEncodedName(entityType, encodedFieldName).getMemberName();
    }

    @Override
    public Type getFieldType(Class<?> entityType, String fieldName) {
        return searchFieldMetadataRegistry.get(entityType, fieldName).getMemberType();
    }

    @Override
    public IndexType getIndexType(Class<?> entityType, String fieldName) {
        return searchFieldMetadataRegistry.get(entityType, fieldName).getIndexType();
    }

    @Override
    public boolean hasIndexedFields(Class<?> entityType) {
        return !searchFieldMetadataRegistry.get(entityType).isEmpty();
    }
}
