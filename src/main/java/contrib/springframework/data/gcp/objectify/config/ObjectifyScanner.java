package contrib.springframework.data.gcp.objectify.config;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Configure Objectify by scanning all classes annotated with {@link Entity} within a base package or its descendants. Allows
 * configuring additional classes manually that may fall outside of this package (e.g. from external libraries).
 */
public class ObjectifyScanner {

    private final String basePackage;
    private final Set<Class<?>> additionalClasses = new HashSet<>();

    public ObjectifyScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    public ObjectifyScanner withAdditionalClasses(Class... classes) {
        Stream.of(classes)
                .forEach(additionalClasses::add);
        return this;
    }

    public void registerEntityClasses() {
        getAnnotatedClasses()
                .forEach(ObjectifyService::register);

        additionalClasses.forEach(ObjectifyService::register);
    }

    Stream<Class<?>> getAnnotatedClasses() {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class, false, false));
        return scanner.findCandidateComponents(basePackage).stream()
                .map(this::beanClass);
    }

    @SuppressWarnings("unchecked")
    private Class<?> beanClass(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new InitializationException(String.format("Class not found %s", beanDefinition.getBeanClassName()), e);
        }
    }
}
