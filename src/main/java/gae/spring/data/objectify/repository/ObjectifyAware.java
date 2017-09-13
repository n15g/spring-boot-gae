package gae.spring.data.objectify.repository;

import com.googlecode.objectify.Objectify;
import gae.spring.data.objectify.ObjectifyProxy;

/**
 * Mark a class as aware of Objectify.
 * <p>
 * Exposes the {@link ObjectifyProxy} via the {@link #ofy()} method.
 */
public interface ObjectifyAware {

    /**
     * @return Objectify.
     */
    Objectify ofy();
}