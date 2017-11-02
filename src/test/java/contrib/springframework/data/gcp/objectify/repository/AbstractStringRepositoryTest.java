package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.objectify.TestStringEntityFixture;

public abstract class AbstractStringRepositoryTest extends ObjectifyTest {
    protected TestStringEntityFixture fixture = new TestStringEntityFixture();

    protected TestStringEntity load(String id) {
        return ofy().load().key(Key.create(TestStringEntity.class, id)).now();
    }
}
