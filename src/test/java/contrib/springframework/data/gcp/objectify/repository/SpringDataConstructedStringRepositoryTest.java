package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.springframework.beans.factory.annotation.Autowired;


public class SpringDataConstructedStringRepositoryTest extends StringSearchRepositoryTests {

    @Autowired
    private TestStringEntityRepository repository;

    @Override
    public ObjectifyRepository<TestStringEntity, String> getRepository() {
        return repository;
    }
}
