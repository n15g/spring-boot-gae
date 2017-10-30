package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import contrib.springframework.data.gcp.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = LongSearchRepositoryTest.Config.class)
public class LongSearchRepositoryTest extends LongSearchRepositoryTests {

    @Autowired
    private TestLongSearchRepository repository;

    @Override
    public SearchRepository<TestLongEntity, Long> getRepository() {
        return repository;
    }

    @Configuration
    public static class Config {
        @Bean
        public TestLongSearchRepository testRepository(SearchService searchService) {
            return new TestLongSearchRepository(searchService);
        }
    }

    public static class TestLongSearchRepository extends AbstractSearchRepository<TestLongEntity, Long> {
        public TestLongSearchRepository(SearchService searchService) {
            super(searchService);
        }
    }
}
