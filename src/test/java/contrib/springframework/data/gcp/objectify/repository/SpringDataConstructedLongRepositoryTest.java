package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import org.springframework.beans.factory.annotation.Autowired;


public class SpringDataConstructedLongRepositoryTest extends LongSearchRepositoryTests {

    @Autowired
    private TestLongEntityRepository repository;

    @Override
    public ObjectifyRepository<TestLongEntity, Long> getRepository() {
        return repository;
    }
}
