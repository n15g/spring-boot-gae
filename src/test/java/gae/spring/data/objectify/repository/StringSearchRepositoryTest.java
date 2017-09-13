package gae.spring.data.objectify.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import gae.spring.data.objectify.TestStringEntity;


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
        public TestStringSearchRepository testRepository() {
            return new TestStringSearchRepository();
        }
    }

    public static class TestStringSearchRepository extends AbstractSearchRepository<TestStringEntity, String> {
    }
}
