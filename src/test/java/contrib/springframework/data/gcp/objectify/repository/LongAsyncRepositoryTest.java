package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = LongAsyncRepositoryTest.Config.class)
public class LongAsyncRepositoryTest extends LongAsyncRepositoryTests {

    @Autowired
    private TestLongAsyncRepository repository;

    @Override
    public AsyncRepository<TestLongEntity, Long> getRepository() {
        return repository;
    }

    @Configuration
    public static class Config {
        @Bean
        public TestLongAsyncRepository testRepository() {
            return new TestLongAsyncRepository();
        }
    }

    public static class TestLongAsyncRepository extends AbstractAsyncRepository<TestLongEntity, Long> {
    }
}
