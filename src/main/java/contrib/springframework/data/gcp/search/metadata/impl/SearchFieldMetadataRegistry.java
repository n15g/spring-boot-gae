package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.SearchId;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.SearchFieldMetadata;

import java.util.Map;

/**
 * Registry of search field accessors for searchable entities.
 * Attempting to get the accessors of an unregistered entity will perform an immediate, inline registration.
 */
public interface SearchFieldMetadataRegistry {
    /**
     * Register an entity class.
     *
     * @param entityClass The entity class to register.
     * @return Registry for method chaining.
     */
    SearchFieldMetadataRegistry register(Class<?> entityClass);

    /**
     * Get a member {@link SearchFieldMetadata} for the given entity class.
     *
     * @param entityClass The entity class.
     * @param memberName  Java name of the member. (Not the index or encoded name).
     * @return SearchFieldMetadata.
     * @throws IllegalArgumentException If the given member name is not that of an {@link SearchIndex indexed} member.
     */
    SearchFieldMetadata get(Class<?> entityClass, String memberName);

    /**
     * Get a member {@link SearchFieldMetadata} for the given entity class by the encoded member name.
     *
     * @param entityClass The entity class.
     * @param encodedName Encoded member name.
     * @return SearchFieldMetadata.
     * @throws IllegalArgumentException If the given encoded name is not that of an {@link SearchIndex indexed} member.
     */
    SearchFieldMetadata getByEncodedName(Class<?> entityClass, String encodedName);

    /**
     * Get all member {@link SearchFieldMetadata accessors} for the given entity class.
     *
     * @param entityClass The entity class.
     * @return Map of accessors keyed by member name.
     */
    Map<String, SearchFieldMetadata> get(Class<?> entityClass);

    /**
     * Get the accessor for the {@link SearchId} member of the given entity class.
     *
     * @param entityClass The entity class.
     * @return Id member accessor.
     */
    SearchFieldMetadata getIdField(Class<?> entityClass);
}
