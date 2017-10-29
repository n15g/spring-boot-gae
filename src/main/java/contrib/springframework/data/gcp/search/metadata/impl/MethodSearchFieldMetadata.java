package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.metadata.SearchFieldMetadata;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * {@link Method} {@link SearchFieldMetadata}.
 */
public class MethodSearchFieldMetadata implements SearchFieldMetadata {
    private static final Function<Method, String> NAME_CALCULATOR = new MethodNameCalculator();
    private static final Function<String, String> NAME_ENCODER = new FieldNameEncoder();

    private final Class<?> entityType;
    private final Method method;
    private final String indexName;
    private final String encodedName;
    private final IndexType indexType;

    /**
     * Create a new instance.
     *
     * @param entityType Entity class.
     * @param method     For this method.
     * @param indexType  Search index type for this method.
     */
    public MethodSearchFieldMetadata(Class<?> entityType, Method method, IndexType indexType) {
        Assert.isTrue(indexType != IndexType.AUTO, "Cannot construct an SearchFieldMetadata with index type AUTO. Use an IndexTypeRegistry instead.");

        this.entityType = entityType;
        this.method = method;
        this.indexName = NAME_CALCULATOR.apply(method);
        this.encodedName = NAME_ENCODER.apply(indexName);
        this.indexType = indexType;
    }

    /**
     * Create a new instance.
     *
     * @param entityType        Entity class.
     * @param method            For this method.
     * @param indexTypeRegistry Determine the index type with this lookup.
     */
    public MethodSearchFieldMetadata(Class<?> entityType, Method method, IndexTypeRegistry indexTypeRegistry) {
        this.entityType = entityType;
        this.method = method;
        this.indexName = NAME_CALCULATOR.apply(method);
        this.encodedName = NAME_ENCODER.apply(indexName);

        SearchIndex annotation = method.getDeclaredAnnotation(SearchIndex.class);
        if (annotation == null || annotation.type() == IndexType.AUTO) {
            this.indexType = indexTypeRegistry.apply(method.getGenericReturnType());
        } else {
            this.indexType = annotation.type();
        }
    }

    @Override
    public Class<?> getEntityType() {
        return entityType;
    }

    @Override
    public Method getMember() {
        return method;
    }

    @Override
    public Type getMemberType() {
        return method.getGenericReturnType();
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
            return method.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
