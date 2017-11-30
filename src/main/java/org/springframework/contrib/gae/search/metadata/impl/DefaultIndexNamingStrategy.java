package org.springframework.contrib.gae.search.metadata.impl;

import org.springframework.contrib.gae.search.metadata.IndexNamingStrategy;

/**
 * Default index naming strategy.
 * Uses the class name as the index name.
 */
public class DefaultIndexNamingStrategy implements IndexNamingStrategy {
    @Override
    public String apply(Class<?> aClass) {
        return aClass.getSimpleName();
    }
}
