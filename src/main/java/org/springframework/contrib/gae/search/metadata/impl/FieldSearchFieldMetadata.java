package org.springframework.contrib.gae.search.metadata.impl;

import org.springframework.contrib.gae.search.SearchIndex;
import org.springframework.contrib.gae.search.metadata.SearchFieldMetadata;
import org.springframework.contrib.gae.search.metadata.IndexTypeRegistry;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * {@link Field} {@link SearchFieldMetadata}.
 */
public class FieldSearchFieldMetadata implements SearchFieldMetadata {
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
    public FieldSearchFieldMetadata(Class<?> entityType, Field field, IndexType indexType) {
        Assert.isTrue(indexType != IndexType.AUTO, "Cannot construct an SearchFieldMetadata with index type AUTO. Use an IndexTypeRegistry instead.");

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
    public FieldSearchFieldMetadata(Class<?> entityType, Field field, IndexTypeRegistry indexTypeRegistry) {
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
