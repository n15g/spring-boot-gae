package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.contrib.gae.objectify.ObjectifyTest;
import org.springframework.contrib.gae.objectify.TestStringEntity;
import org.junit.Test;

public class EntityNotFoundExceptionTest extends ObjectifyTest {

    @Test
    public void construct() throws Exception {
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("No entity was found matching the key: Key<?>(TestStringEntity(\"id\"))");
        throw new EntityNotFoundException(Key.create(TestStringEntity.class, "id"));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void construct_willThrowException_whenKeyIsNull() throws Exception {
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("No entity was found matching the key: null");
        throw new EntityNotFoundException(null);
    }
}