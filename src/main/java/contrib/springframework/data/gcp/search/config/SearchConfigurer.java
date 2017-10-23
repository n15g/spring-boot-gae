package contrib.springframework.data.gcp.search.config;

import contrib.springframework.data.gcp.search.SearchService;
import contrib.springframework.data.gcp.search.metadata.IndexTypeRegistry;
import org.springframework.core.convert.ConversionService;

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

    /**
     * Register additional Search {@link org.springframework.core.convert.converter.Converter}s, or otherwise manipulate the
     * search {@link ConversionService}.
     *
     * @param conversionService The search conversion service.
     */
    default void registerSearchConverters(ConversionService conversionService) {
        //no-op
    }
}
