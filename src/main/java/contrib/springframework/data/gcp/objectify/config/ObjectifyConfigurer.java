package contrib.springframework.data.gcp.objectify.config;

import com.googlecode.objectify.impl.translate.TranslatorFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * Extending an {@link org.springframework.context.annotation.Configuration} class with ObjectifyConfigurer will allow you to customize the
 * configuration of the Objectify service created by the {@link ObjectifyAutoConfiguration} class.
 * This interface defines a collection of convenience callbacks that ensure entities, translators, etc. are registered at the appropriate time in the Objectify
 * lifecycle and, in particular, prior to any use of the objectify service, ensuring that all entities are registered before use.
 */
public interface ObjectifyConfigurer {

    /**
     * Register translators with Objectify.
     * Translators registered here will override any matching translators registered in {@link ObjectifyAutoConfiguration}.
     *
     * @return Collection of entities to register.
     */
    default Collection<TranslatorFactory<?, ?>> registerObjectifyTranslators() {
        return Collections.emptyList();
    }

    /**
     * Register entities with Objectify.
     *
     * @return Collection of entities to register.
     */
    default Collection<Class<?>> registerObjectifyEntities() {
        return Collections.emptyList();
    }
}
