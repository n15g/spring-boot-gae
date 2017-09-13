package gae.spring.data.objectify;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

@Configuration
@Import(ObjectifyAutoConfiguration.class)
public class ObjectifyTestConfiguration implements ObjectifyConfigurer {
    @Override
    public List<Class<?>> registerObjectifyEntities() {
        return Arrays.asList(
                TestStringEntity.class,
                TestLongEntity.class
        );
    }
}
