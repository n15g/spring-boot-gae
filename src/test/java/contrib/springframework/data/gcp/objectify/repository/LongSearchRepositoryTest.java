package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
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
        public TestLongSearchRepository testRepository() {
            return new TestLongSearchRepository();
        }
    }

    public static class TestLongSearchRepository extends AbstractSearchRepository<TestLongEntity, Long> {
    }
}
