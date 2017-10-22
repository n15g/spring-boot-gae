package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
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