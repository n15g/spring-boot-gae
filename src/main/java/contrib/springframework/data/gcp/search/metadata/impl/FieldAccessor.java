package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.metadata.Accessor;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import contrib.springframework.data.gcp.search.IndexType;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * {@link Field} {@link Accessor}.
 */
public class FieldAccessor implements Accessor {
    private static final Function<Field, String> NAME_CALCULATOR = new FieldNameLocator();
    private static final Function<String, String> NAME_ENCODER = new FieldNameEncoder();

    private final Class<?> entityType;
    private final Field field;
    private final String indexName;
    private final String encodedName;
    private final IndexType indexType;

    /**
     * Create a new instance.
     *
     * @param entityType Entity class.
     * @param field      For this field.
     * @param indexType  Search index type for this field.
     */
    public FieldAccessor(Class<?> entityType, Field field, IndexType indexType) {
        Assert.isTrue(indexType != IndexType.AUTO, "Cannot construct an Accessor with index type AUTO. Use an IndexTypeRegistry instead.");

        this.entityType = entityType;
        this.field = field;
        this.indexName = NAME_CALCULATOR.apply(field);
        this.encodedName = NAME_ENCODER.apply(indexName);
        this.indexType = indexType;
    }

    /**
     * Create a new instance.
     *
     * @param entityType        Entity class.
     * @param field             For this field.
     * @param indexTypeRegistry Determine the index type with this lookup.
     */
    public FieldAccessor(Class<?> entityType, Field field, IndexTypeRegistry indexTypeRegistry) {
        this.entityType = entityType;
        this.field = field;
        this.indexName = NAME_CALCULATOR.apply(field);
        this.encodedName = NAME_ENCODER.apply(indexName);

        SearchIndex annotation = field.getDeclaredAnnotation(SearchIndex.class);
        if (annotation == null || annotation.type() == IndexType.AUTO) {
            this.indexType = indexTypeRegistry.apply(field.getGenericType());
        } else {
            this.indexType = annotation.type();
        }
    }

    @Override
    public Class<?> getEntityType() {
        return entityType;
    }

    @Override
    public Field getMember() {
        return field;
    }

    @Override
    public Type getMemberType() {
        return field.getGenericType();
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public String getEncodedName() {
        return encodedName;
    }

    @Override
    public IndexType getIndexType() {
        return indexType;
    }

    @Override
    public Object getValue(Object entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
