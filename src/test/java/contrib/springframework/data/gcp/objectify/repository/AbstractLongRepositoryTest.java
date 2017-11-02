package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.objectify.TestLongEntity;
import contrib.springframework.data.gcp.objectify.TestLongEntityFixture;

public abstract class AbstractLongRepositoryTest extends ObjectifyTest {
    protected TestLongEntityFixture fixture = new TestLongEntityFixture();

    protected TestLongEntity load(Long id) {
        return ofy().load().key(Key.create(TestLongEntity.class, id)).now();
    }
}
