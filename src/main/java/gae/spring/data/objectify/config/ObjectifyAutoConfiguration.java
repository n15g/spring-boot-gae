package gae.spring.data.objectify.config;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.impl.translate.Translators;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;
import gae.spring.data.objectify.ObjectifyProxy;
import gae.spring.data.objectify.translator.Jsr310Translators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatic objectify configuration.
 * Provides the following beans and services:
 * <ul>
 * <li>Registers an {@link ObjectifyProxy} configured by any registered {@link ObjectifyConfigurer} beans.</li>
 * <li>Registers the {@link ObjectifyFilter} to manage Objectify sessions per-request.</li>
 * </ul>
 */
@Configuration
@ConditionalOnClass(Objectify.class)
@ConditionalOnMissingBean(ObjectifyProxy.class)
@Import(ObjectifyRepositoriesAutoConfigurationRegistrar.class)
public class ObjectifyAutoConfiguration {

    List<ObjectifyConfigurer> configurers = new ArrayList<>();

    @Autowired(required = false)
    public void setConfigurers(List<ObjectifyConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers = configurers;
        }
    }

    /**
     * @return Register the default {@link ObjectifyProxy}.
     */
    @Bean
    public ObjectifyProxy ofy() {
        ObjectifyProxy objectify = new ObjectifyProxy() {
        };

        registerTranslators(objectify.factory().getTranslators());
        registerEntities(objectify);


        return objectify;
    }

    private void registerTranslators(Translators translators) {
        configurers.stream()
                .flatMap(configurer -> configurer.registerObjectifyTranslators().stream())
                .forEach(translators::add);

        Jsr310Translators.addTo(translators);
        translators.add(new BigDecimalLongTranslatorFactory()); //Default factor of 3. Override with ObjectifyConfigurer if you need higher precision.
    }

    private void registerEntities(ObjectifyProxy objectify) {
        configurers.forEach(configurer -> objectify.register(configurer.registerObjectifyEntities()));
    }

    /**
     * @return Register the {@link ObjectifyFilter}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectifyFilter objectifyFilter() {
        return new ObjectifyFilter();
    }
}
