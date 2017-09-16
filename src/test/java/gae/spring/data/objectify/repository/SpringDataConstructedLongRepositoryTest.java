package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.TestLongEntity;
import org.springframework.beans.factory.annotation.Autowired;


public class SpringDataConstructedLongRepositoryTest extends LongSearchRepositoryTests {

    @Autowired
    private TestLongEntityRepository repository;

    @Override
    public ObjectifyRepository<TestLongEntity, Long> getRepository() {
        return repository;
    }
}
