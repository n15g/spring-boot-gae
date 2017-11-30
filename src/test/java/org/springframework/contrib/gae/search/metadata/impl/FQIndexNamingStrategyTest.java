package org.springframework.contrib.gae.search.metadata.impl;

import org.springframework.contrib.gae.search.metadata.IndexNamingStrategy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FQIndexNamingStrategyTest {
    private IndexNamingStrategy strategy = new FQIndexNamingStrategy();

    @Test
    public void apply() {
        assertThat(strategy.apply(String.class)).isEqualTo("java.lang.String");
        assertThat(strategy.apply(FQIndexNamingStrategy.class)).isEqualTo("org.springframework.contrib.gae.search.metadata.impl.FQIndexNamingStrategy");
        assertThat(strategy.apply(FQIndexNamingStrategyTest.InnerClass.class)).isEqualTo("org.springframework.contrib.gae.search.metadata.impl.FQIndexNamingStrategyTest$InnerClass");
    }

    private static final class InnerClass {

    }
}