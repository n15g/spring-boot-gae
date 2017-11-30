package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.contrib.gae.objectify.ObjectifyTest;
import org.springframework.contrib.gae.objectify.TestStringEntity;
import org.springframework.contrib.gae.objectify.TestStringEntityFixture;

public abstract class AbstractStringRepositoryTest extends ObjectifyTest {
    protected TestStringEntityFixture fixture = new TestStringEntityFixture();

    protected TestStringEntity load(String id) {
        return ofy().load().key(Key.create(TestStringEntity.class, id)).now();
    }
}
