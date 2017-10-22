package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TestStringEntityRepository extends ObjectifyRepository<TestStringEntity, String> {
}
