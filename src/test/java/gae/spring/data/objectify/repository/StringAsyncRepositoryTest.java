package gae.spring.data.objectify.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import gae.spring.data.objectify.TestStringEntity;

@ContextConfiguration(classes = StringAsyncRepositoryTest.Config.class)
public class StringAsyncRepositoryTest extends StringAsyncRepositoryTests {

    @Autowired
    private TestStringAsyncRepository repository;

    @Override
    public AsyncRepository<TestStringEntity, String> getRepository() {
        return repository;
    }


    @Configuration
    public static class Config {
        @Bean
        public TestStringAsyncRepository testRepository() {
            return new TestStringAsyncRepository();
        }
    }

    public static class TestStringAsyncRepository extends AbstractAsyncRepository<TestStringEntity, String> {
    }
}
