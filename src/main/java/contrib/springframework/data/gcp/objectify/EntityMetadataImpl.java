package contrib.springframework.data.gcp.objectify;

import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic {@link EntityMetadata} implementation.
 */
public class EntityMetadataImpl implements EntityMetadata {
    private final ObjectifyProxy objectify;

    private final Map<Class<?>, Field> idFieldCache = new HashMap<>();
    private final Map<Class<?>, Class<?>> idTypeCache = new HashMap<>();

    /**
     * Create a new instance.
     *
     * @param objectify Objectify proxy.
     */
    public EntityMetadataImpl(ObjectifyProxy objectify) {
        this.objectify = objectify;
    }

    @Nonnull
    @Override
    public <E> Field getIdField(Class<E> entityClass) {
        return idFieldCache.computeIfAbsent(entityClass, this::getIdFieldFromObjectify);
    }

    @Nonnull
    @Override
    public <E> Class<?> getIdType(Class<E> entityClass) {
        return idTypeCache.computeIfAbsent(entityClass, this::getIdTypeFromObjectify);
    }

    protected <E> Field getIdFieldFromObjectify(Class<E> entityClass) {
        String idFieldName = objectify.ofy()
                .factory()
                .getMetadata(entityClass)
                .getKeyMetadata()
                .getIdFieldName();
        return FieldUtils.getField(entityClass, idFieldName, true);
    }

    protected <E> Class<?> getIdTypeFromObjectify(Class<E> entityClass) {
        return objectify.ofy()
                .factory()
                .getMetadata(entityClass)
                .getKeyMetadata()
                .getIdFieldType();
    }
}
