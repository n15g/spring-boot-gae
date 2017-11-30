package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Objectify;
import org.springframework.contrib.gae.objectify.ObjectifyProxy;

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