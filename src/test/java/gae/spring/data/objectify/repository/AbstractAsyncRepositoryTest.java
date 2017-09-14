package gae.spring.data.objectify.repository;

import gae.spring.data.objectify.ObjectifyProxy;
import gae.spring.data.objectify.ObjectifyTest;
import gae.spring.data.objectify.TestStringEntity;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractAsyncRepositoryTest extends ObjectifyTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Autowired
    private ObjectifyProxy objectify;

    @Test
    public void resolveTypes() throws Exception {
        EntityManager<TestStringEntity, Long> manager = new SomeEntityManager();
        assertThat(manager.getEntityType(), equalTo(TestStringEntity.class));
        assertThat(manager.getIdType(), equalTo(Long.class));
    }

    @Test
    public void resolveTypesMultipleLayersDown() throws Exception {
        EntityManager<TestStringEntity, String> manager = new RepositoryC();
        assertThat(manager.getEntityType(), equalTo(TestStringEntity.class));
        assertThat(manager.getIdType(), equalTo(String.class));
    }

    @Test
    public void testOfy() throws Exception {
        SomeEntityManager entityManager = new SomeEntityManager();
        entityManager.setObjectify(objectify);

        assertThat(entityManager.ofy(), sameInstance(objectify.ofy()));
    }

    @Test
    public void getIdField() throws Exception {
        Field expectedField = TestStringEntity.class.getDeclaredField("id");
        SomeEntityManager entityManager = new SomeEntityManager();
        entityManager.setObjectify(objectify);
        assertThat(entityManager.getIdField(), equalTo(expectedField));
    }

    private class SomeEntityManager extends AbstractAsyncRepository<TestStringEntity, Long> {
    }

    private class RepositoryA<E, I extends Serializable> extends AbstractAsyncRepository<E, I> {
    }

    private class RepositoryB<E, I extends Serializable> extends RepositoryA<E, I> {
    }

    private class RepositoryC extends RepositoryB<TestStringEntity, String> {
    }
}