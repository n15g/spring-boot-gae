package gae.spring.data.objectify.repository;

import java.io.Serializable;

/**
 * Basic repository.
 * <p>
 * Builds upon {@link AsyncRepository}, adding synchronous access methods.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
public abstract class AbstractRepository<E, I extends Serializable> extends AbstractAsyncRepository<E, I> implements Repository<E, I> {
}
