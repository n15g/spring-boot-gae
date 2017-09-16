package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.TestLongEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TestLongEntityRepository extends ObjectifyRepository<TestLongEntity, Long> {
}
