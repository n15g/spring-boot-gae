package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = LongRepositoryTest.Config.class)
public class LongRepositoryTest extends LongRepositoryTests {

    @Autowired
    private TestLongRepository repository;

    @Override
    public Repository<TestLongEntity, Long> getRepository() {
        return repository;
    }


    @Configuration
    public static class Config {
        @Bean
        public TestLongRepository testRepository() {
            return new TestLongRepository();
        }
    }

    public static class TestLongRepository extends AbstractRepository<TestLongEntity, Long> {
    }
}
