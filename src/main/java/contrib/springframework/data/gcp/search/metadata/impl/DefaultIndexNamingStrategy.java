package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;

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
