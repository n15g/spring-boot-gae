package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.metadata.Accessor;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link SearchMetadata} implementation.
 */
public class SearchMetadataImpl implements SearchMetadata {
    private final AccessorRegistry accessorRegistry;

    /**
     * Create a new instance.
     *
     * @param indexTypeRegistry Lookup used to determine index type for a field.
     */
    public SearchMetadataImpl(IndexTypeRegistry indexTypeRegistry) {
        this.accessorRegistry = new AccessorRegistryImpl(indexTypeRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E, I> I getId(E entity) {
        return (I) accessorRegistry
                .getIdAccessor(entity.getClass())
                .getValue(entity);
    }

    @Override
    public Map<String, Accessor> getSearchFields(Object entity) {
        return accessorRegistry.get(entity.getClass());
    }

    @Override
    public String encodeFieldName(Class<?> entityType, String fieldName) {
        return accessorRegistry.get(entityType, fieldName).getEncodedName();
    }

    @Override
    public String decodeFieldName(Class<?> entityType, String encodedFieldName) {
        return accessorRegistry.getByEncodedName(entityType, encodedFieldName).getMemberName();
    }

    @Override
    public Type getFieldType(Class<?> entityType, String fieldName) {
        return accessorRegistry.get(entityType, fieldName).getMemberType();
    }

    @Override
    public IndexType getIndexType(Class<?> entityType, String fieldName) {
        return accessorRegistry.get(entityType, fieldName).getIndexType();
    }

    @Override
    public boolean hasIndexedFields(Class<?> entityType) {
        return !accessorRegistry.get(entityType).isEmpty();
    }
}
