package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.TestStringEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TestStringEntityRepository extends ObjectifyRepository<TestStringEntity, String> {
}
