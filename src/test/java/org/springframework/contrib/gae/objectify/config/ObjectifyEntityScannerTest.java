package org.springframework.contrib.gae.objectify.config;

import org.springframework.contrib.gae.objectify.config.sample.TestEntity1;
import org.springframework.contrib.gae.objectify.config.sample.subpackage.TestEntity2;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class ObjectifyEntityScannerTest {

    @Test
    public void getAnnotatedClasses_willReturnAnnotatedClassesWithAdditionalClasses_andIncludeSubPackages() {

        Set<Class<?>> entityClasses = new ObjectifyEntityScanner("org.springframework.contrib.gae.objectify.config.sample")
                .withAdditionalClasses(Integer.class)
                .getEntityClasses();

        assertThat(entityClasses, containsInAnyOrder(TestEntity1.class, TestEntity2.class, Integer.class));
    }

}