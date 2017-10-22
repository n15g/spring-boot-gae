package contrib.springframework.data.gcp.search.config;

import contrib.springframework.data.gcp.search.SearchService;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;

/**
 * Extending an {@link org.springframework.context.annotation.Configuration} class with SearchConfigurer will allow you to customize the
 * configuration of the GCP Search Service created by the {@link SearchAutoConfiguration} class.
 */
public interface SearchConfigurer {

    /**
     * Register additional {@link SearchService} index types.
     *
     * @param registry Index type registry.
     */
    default void registerSearchIndexTypes(IndexTypeRegistry registry) {
        //no-op
    }
}
