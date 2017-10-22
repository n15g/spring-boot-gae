package contrib.springframework.data.gcp.objectify.config;

import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableObjectifyRepositories} annotation.
 */
class ObjectifyRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableObjectifyRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new ObjectifyRepositoryConfigurationExtension();
    }
}
