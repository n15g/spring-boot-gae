package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Objectify;
import contrib.springframework.data.gcp.objectify.ObjectifyProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.util.Pair;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Basic repository scaffolding.
 * Handles resolving entity and id types, configuring the search service and obtaining an objectify reference.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class AbstractAsyncRepository<E, I extends Serializable> implements AsyncRepository<E, I> {

    protected final Class<E> entityType;
    protected final Class<I> idType;
    protected Field idField;

    private ObjectifyProxy objectify;

    /**
     * Create a new instance.
     * Resolves the entity and id types.
     */
    protected AbstractAsyncRepository() {
        Pair<Class<E>, Class<I>> types = resolveTypes();
        this.entityType = types.getFirst();
        this.idType = types.getSecond();
    }

    @Autowired
    public AbstractAsyncRepository setObjectify(ObjectifyProxy objectify) {
        this.objectify = objectify;
        return this;
    }

    @Nonnull
    @Override
    public Class<E> getEntityType() {
        return entityType;
    }

    @Nonnull
    @Override
    public Class<I> getIdType() {
        return idType;
    }

    @Nonnull
    @Override
    public Field getIdField() {
        if (idField == null) {
            //Cache the id field, rather than making #getIdField() calculate it every time.
            this.idField = AsyncRepository.super.getIdField();
        }
        return idField;
    }

    @Override
    public Objectify ofy() {
        return objectify.ofy();
    }

    @SuppressWarnings("unchecked")
    private Pair<Class<E>, Class<I>> resolveTypes() {
        ResolvableType[] types = ResolvableType.forClass(this.getClass())
                .as(AbstractAsyncRepository.class)
                .getGenerics();
        return Pair.of((Class<E>) types[0].resolve(), (Class<I>) types[1].resolve());
    }
}
