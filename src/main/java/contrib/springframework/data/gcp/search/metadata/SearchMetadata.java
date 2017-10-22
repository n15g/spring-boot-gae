package contrib.springframework.data.gcp.search.metadata;

import contrib.springframework.data.gcp.search.IndexType;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Holds metadata about a searchable entity.
 */
public interface SearchMetadata {

    /**
     * Get the id of the given search entity.
     *
     * @param entity The entity.
     * @param <E>    Entity type.
     * @param <I>    Id type.
     * @return Entity id.
     * @throws IllegalArgumentException If the entity does not have a {@link contrib.springframework.data.gcp.search.SearchId} annotated field.
     */
    <E, I> I getId(E entity);

    /**
     * Return a map of indexed field values for the given entity.
     *
     * @param entity The entity.
     * @return Map of field values.
     */
    Map<String, Accessor> getSearchFields(Object entity);

    /**
     * Encode the name of a field for use in a search index.
     *
     * @param entityType The entity type.
     * @param fieldName  The name of the field.
     * @return Encoded field name.
     * @throws IllegalArgumentException If the given field does not exist.
     */
    String encodeFieldName(Class<?> entityType, String fieldName);

    /**
     * Decode the name of a field from a search index.
     *
     * @param entityType       The entity type.
     * @param encodedFieldName The encoded field name.
     * @return Decoded field name.
     * @throws IllegalArgumentException If the given field does not exist.
     */
    String decodeFieldName(Class<?> entityType, String encodedFieldName);

    /**
     * Get the java type of a field.
     *
     * @param entityType The entity type.
     * @param fieldName  The name of the field.
     * @return Field type.
     * @throws IllegalArgumentException If the given field does not exist.
     */
    Type getFieldType(Class<?> entityType, String fieldName);

    /**
     * Return the data type of an indexed field.
     *
     * @param entityType The entity type.
     * @param fieldName  The name of the field.
     * @return Index type.
     * @throws IllegalArgumentException If the given field does not exist.
     */
    IndexType getIndexType(Class<?> entityType, String fieldName);

    /**
     * @param entityType The entity type.
     * @return True if the entity has fields marked as indexed for searching.
     */
    boolean hasIndexedFields(Class<?> entityType);
}
