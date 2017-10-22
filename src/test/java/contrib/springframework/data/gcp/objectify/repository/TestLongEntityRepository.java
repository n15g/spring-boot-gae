package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TestLongEntityRepository extends ObjectifyRepository<TestLongEntity, Long> {
}
