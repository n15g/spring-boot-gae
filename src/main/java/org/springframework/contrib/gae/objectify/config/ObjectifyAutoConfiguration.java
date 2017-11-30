package org.springframework.contrib.gae.objectify.config;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.impl.translate.Translators;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;
import org.springframework.contrib.gae.objectify.ObjectifyProxy;
import org.springframework.contrib.gae.objectify.translator.Jsr310Translators;
import org.springframework.contrib.gae.objectify.EntityMetadata;
import org.springframework.contrib.gae.objectify.EntityMetadataImpl;
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

    /**
     * Gather all the {@link ObjectifyConfigurer} beans registered with the container.
     * These will be used to configure the beans created here.
     *
     * @param configurers Registered {@link ObjectifyConfigurer} list.
     */
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

    /**
     * Set up static access to the Objectify proxy.
     *
     * @param objectify Objectify proxy.
     * @return Objectify proxy static accessor.
     */
    @Bean
    public StaticObjectifyProxy staticObjectifyProxy(ObjectifyProxy objectify) {
        return new StaticObjectifyProxy(objectify);
    }

    /**
     * Register the {@link EntityMetadata} bean.
     *
     * @param objectify Objectify proxy.
     * @return {@link EntityMetadata} bean.
     */
    @Bean
    public EntityMetadata entityMetadata(ObjectifyProxy objectify) {
        return new EntityMetadataImpl(objectify);
    }

    /**
     * @return Register the {@link ObjectifyFilter}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectifyFilter objectifyFilter() {
        return new ObjectifyFilter();
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
}
