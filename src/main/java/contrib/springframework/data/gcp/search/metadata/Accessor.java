package contrib.springframework.data.gcp.search.metadata;

import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * Provides access to a search field or method on an entity class.
 */
public interface Accessor {
    /**
     * @return The entity type this accessor belongs to.
     */
    Class<?> getEntityType();

    /**
     * @return The member being accessed.
     */
    Member getMember();

    /**
     * @return The
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
