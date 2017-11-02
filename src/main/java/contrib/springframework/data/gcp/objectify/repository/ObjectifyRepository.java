package contrib.springframework.data.gcp.objectify.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Base repository type for {@link org.springframework.stereotype.Repository} injected objectify repositories.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface ObjectifyRepository<E, I extends Serializable> extends SearchRepository<E, I> {
}
