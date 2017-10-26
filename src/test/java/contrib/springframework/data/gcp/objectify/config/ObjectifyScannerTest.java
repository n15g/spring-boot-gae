package contrib.springframework.data.gcp.objectify.config;

import contrib.springframework.data.gcp.objectify.config.sample.TestEntity1;
import contrib.springframework.data.gcp.objectify.config.sample.subpackage.TestEntity2;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class ObjectifyScannerTest {

    @Test
    public void getAnnotatedClasses_willReturnAnnotatedClasses_andIncludeSubPackages() {

        Set<Class<?>> entityClasses = new ObjectifyScanner("contrib.springframework.data.gcp.objectify.config.sample")
                .getAnnotatedClasses()
                .collect(Collectors.toSet());

        assertThat(entityClasses, containsInAnyOrder(TestEntity1.class, TestEntity2.class));
    }

}