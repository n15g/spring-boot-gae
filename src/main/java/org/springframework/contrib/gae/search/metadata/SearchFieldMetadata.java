package org.springframework.contrib.gae.search.metadata;

import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.SearchIndex;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * Provides access to a search field or method on an entity class.
 */
public interface SearchFieldMetadata {
    /**
     * @return The entity type this accessor belongs to.
     */
    Class<?> getEntityType();

    /**
     * @return The java {@link Member} for this search field.
     */
    Member getMember();

    /**
     * @return The name of the java {@link Member} for this search field.
     */
    default String getMemberName() {
        return getMember().getName();
    }

    /**
     * @return The java type of the member.
     */
    Type getMemberType();

    /**
     * @return The declared or calculated name of the index.
     * @see SearchIndex#name()
     */
    String getIndexName();

    /**
     * @return The encoded name used as the search index name.
     */
    String getEncodedName();

    /**
     * @return The index type of the field.
     */
    IndexType getIndexType();

    /**
     * Get the field value for the given entity.
     *
     * @param entity The entity.
     * @return Field value.
     */
    Object getValue(Object entity);
}
