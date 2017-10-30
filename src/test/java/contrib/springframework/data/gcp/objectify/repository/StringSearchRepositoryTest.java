package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = StringSearchRepositoryTest.Config.class)
public class StringSearchRepositoryTest extends StringSearchRepositoryTests {

    @Autowired
    private TestStringSearchRepository repository;

    @Override
    public SearchRepository<TestStringEntity, String> getRepository() {
        return repository;
    }

    @Configuration
    public static class Config {
        @Bean
        public TestStringSearchRepository testRepository(SearchService searchService) {
            return new TestStringSearchRepository(searchService);
        }
    }

    public static class TestStringSearchRepository extends AbstractSearchRepository<TestStringEntity, String> {
        public TestStringSearchRepository(SearchService searchService) {
            super(searchService);
        }
    }
}
