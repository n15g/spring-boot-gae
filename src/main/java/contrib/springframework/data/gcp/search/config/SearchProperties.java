package contrib.springframework.data.gcp.search.config;

import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;
import contrib.springframework.data.gcp.search.metadata.impl.DefaultIndexNamingStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * GCP Search configuration properties.
 */
@Component
@ConfigurationProperties("spring.data.gcp.search")
public class SearchProperties {

    /**
     * Search index naming strategy to use.
     */
    private Class<? extends IndexNamingStrategy> indexNamingStrategy = DefaultIndexNamingStrategy.class;

    public Class<? extends IndexNamingStrategy> getIndexNamingStrategy() {
        return indexNamingStrategy;
    }

    public SearchProperties setIndexNamingStrategy(Class<? extends IndexNamingStrategy> indexNamingStrategy) {
        this.indexNamingStrategy = indexNamingStrategy;
        return this;
    }
}
