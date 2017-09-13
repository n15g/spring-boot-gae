package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.TestStringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = StringRepositoryTest.Config.class)
public class StringRepositoryTest extends StringRepositoryTests {

    @Autowired
    private TestStringRepository repository;

    @Override
    public Repository<TestStringEntity, String> getRepository() {
        return repository;
    }

    @Configuration
    public static class Config {
        @Bean
        public TestStringRepository testRepository() {
            return new TestStringRepository();
        }
    }

    public static class TestStringRepository extends AbstractRepository<TestStringEntity, String> {
    }
}
