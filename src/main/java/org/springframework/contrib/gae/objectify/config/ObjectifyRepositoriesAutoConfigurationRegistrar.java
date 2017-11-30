package org.springframework.contrib.gae.objectify.config;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * {@link EnableObjectifyRepositories} in an auto-config environment.
 */
class ObjectifyRepositoriesAutoConfigurationRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableObjectifyRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableObjectifyRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new ObjectifyRepositoryConfigurationExtension();
    }

    @EnableObjectifyRepositories
    private static class EnableObjectifyRepositoriesConfiguration {
    }
}
