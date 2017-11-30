package org.springframework.contrib.gae.objectify.config;

import org.springframework.contrib.gae.objectify.support.AbstractObjectifyRepository;
import org.springframework.contrib.gae.objectify.support.ObjectifyRepositoryFactoryBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

import java.lang.annotation.*;

/**
 * Annotation to enable JPA repositories. Will scan the package of the annotated configuration class for Spring Data
 * repositories by default.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(ObjectifyRepositoriesRegistrar.class)
public @interface EnableObjectifyRepositories {

    /**
     * @return Alias for {@link #basePackages()}.
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * @return Base packages to scan for annotated components. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * @return Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
     * each package that serves no purpose other than being referenced by this attribute.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * @return Which types are eligible for component scanning. Further narrows the set of candidate components from
     * everything in {@link #basePackages()} to everything in the base packages that matches the given filterIn or filters.
     */
    Filter[] includeFilters() default {};

    /**
     * @return Which types are not eligible for component scanning.
     */
    Filter[] excludeFilters() default {};

    /**
     * @return The postfix to be used when looking up custom repository implementations. Defaults to {@literal Impl}. So
     * for a repository named {@code PersonRepository} the corresponding implementation class will be looked up scanning
     * for {@code PersonRepositoryImpl}.
     */
    String repositoryImplementationPostfix() default "Impl";


    /**
     * @return The location of where to find the Spring Data named queries properties file. Will default to
     * {@code META-INF/objectify-named-queries.properties}.
     */
    String namedQueriesLocation() default "";

    /**
     * @return The key of the {@link org.springframework.data.repository.query.QueryLookupStrategy} to be used for lookup queries for filter methods. Defaults to
     * {@link Key#CREATE_IF_NOT_FOUND}.
     */
    Key queryLookupStrategy() default Key.CREATE_IF_NOT_FOUND;

    /**
     * @return The {@link org.springframework.beans.factory.FactoryBean} class to be used for each repository instance. Defaults to
     * {@link ObjectifyRepositoryFactoryBean}.
     */
    Class<?> repositoryFactoryBeanClass() default ObjectifyRepositoryFactoryBean.class;

    /**
     * @return The repository base class to be used to create repository proxies for this particular configuration.
     */
    Class<?> repositoryBaseClass() default AbstractObjectifyRepository.class;
}
