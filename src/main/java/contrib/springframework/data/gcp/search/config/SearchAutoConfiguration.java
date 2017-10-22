package contrib.springframework.data.gcp.search.config;

import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import contrib.springframework.data.gcp.search.metadata.impl.DefaultIndexTypeRegistry;
import contrib.springframework.data.gcp.search.metadata.impl.SearchMetadataImpl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatic GCP Search Service configuration.
 */
@Configuration
@ConditionalOnMissingBean(SearchMetadata.class)
public class SearchAutoConfiguration {

    List<SearchConfigurer> configurers = new ArrayList<>();

    /**
     * @return Search properties bean.
     */
    @Bean
    public SearchProperties searchProperties() {
        return new SearchProperties();
    }

    /**
     * Gather all the {@link SearchConfigurer} beans registered with the container.
     * These will be used to configure the beans created here.
     *
     * @param configurers Registered {@link SearchConfigurer} list.
     */
    @Autowired(required = false)
    public void setConfigurers(List<SearchConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers = configurers;
        }
    }

    /**
     * Register the {@link SearchMetadata} bean.
     *
     * @return {@link SearchMetadata} bean.
     */
    @Bean
    public SearchMetadata entityMetadata() {
        DefaultIndexTypeRegistry registry = new DefaultIndexTypeRegistry();

        configurers.forEach(configurer -> configurer.registerSearchIndexTypes(registry));

        return new SearchMetadataImpl(registry);
    }

    /**
     * @return Index naming strategy bean.
     */
    @Bean
    @ConditionalOnMissingBean(IndexNamingStrategy.class)
    public IndexNamingStrategy indexNamingStrategy() {
        try {
            return searchProperties().getIndexNamingStrategy().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanInitializationException("Could not create IndexNamingStrategy", e);
        }
    }
}
