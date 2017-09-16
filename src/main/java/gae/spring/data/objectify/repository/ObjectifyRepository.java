package gae.spring.data.objectify.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * An Objectify repository implementing {@link AsyncRepository}, {@link Repository} and {@link SearchRepository} functionality.
 *
 * @param <E> Entity type.
 * @param <I> Entity id type.
 */
@NoRepositoryBean
public interface ObjectifyRepository<E, I extends Serializable> extends SearchRepository<E, I> {
}
