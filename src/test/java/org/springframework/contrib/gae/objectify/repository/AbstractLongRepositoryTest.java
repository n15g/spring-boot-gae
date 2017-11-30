package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.contrib.gae.objectify.ObjectifyTest;
import org.springframework.contrib.gae.objectify.TestLongEntity;
import org.springframework.contrib.gae.objectify.TestLongEntityFixture;

public abstract class AbstractLongRepositoryTest extends ObjectifyTest {
    protected TestLongEntityFixture fixture = new TestLongEntityFixture();

    protected TestLongEntity load(Long id) {
        return ofy().load().key(Key.create(TestLongEntity.class, id)).now();
    }
}
