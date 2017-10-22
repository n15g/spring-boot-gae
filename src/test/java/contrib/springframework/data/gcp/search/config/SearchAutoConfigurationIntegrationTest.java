package contrib.springframework.data.gcp.search.config;

import contrib.springframework.data.gcp.search.metadata.IndexNamingStrategy;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import contrib.springframework.data.gcp.search.metadata.impl.DefaultIndexNamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SearchTestConfiguration.class)
@RunWith(SpringRunner.class)
public class SearchAutoConfigurationIntegrationTest {

    @Autowired
    private IndexNamingStrategy indexNamingStrategy;

    @Autowired
    private SearchMetadata searchMetadata;

    @Test
    public void indexNamingStrategy_willBeDefaultIndexNamingStrategy() {
        assertThat(indexNamingStrategy).isInstanceOf(DefaultIndexNamingStrategy.class);
    }

    @Test
    public void searchMetadata() {
        assertThat(searchMetadata).isNotNull();
    }
}