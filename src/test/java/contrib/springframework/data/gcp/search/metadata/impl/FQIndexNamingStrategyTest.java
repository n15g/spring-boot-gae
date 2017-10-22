package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FQIndexNamingStrategyTest {
    private IndexNamingStrategy strategy = new FQIndexNamingStrategy();

    @Test
    public void apply() {
        assertThat(strategy.apply(String.class)).isEqualTo("java.lang.String");
        assertThat(strategy.apply(FQIndexNamingStrategy.class)).isEqualTo("contrib.springframework.data.gcp.search.metadata.impl.FQIndexNamingStrategy");
        assertThat(strategy.apply(FQIndexNamingStrategyTest.InnerClass.class)).isEqualTo("contrib.springframework.data.gcp.search.metadata.impl.FQIndexNamingStrategyTest$InnerClass");
    }

    private static final class InnerClass {

    }
}