package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.objectify.TestStringEntityFixture;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SuppressWarnings("ConstantConditions")
public class EntityManagerTest extends ObjectifyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TestEntityManager entityManager = new TestEntityManager();

    @Test
    public void getIdField() throws Exception {
        Field expectedField = TestStringEntity.class.getDeclaredField("id");
        assertThat(entityManager.getIdField()).isEqualTo(expectedField);
    }

    @Test
    public void getIdField_willThrowException_whenEntityIsNotAnEntity() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No class 'java.lang.String' was registered");
        new TestNotAnEntityManager().getIdField();
    }

    @Test
    public void getKeyClass() throws Exception {
        assertThat(entityManager.getKeyClass()).isEqualTo(Key.class);
    }

    @Test
    public void getKey() throws Exception {
        Key<TestStringEntity> key = entityManager.getKey(new TestStringEntity("id"));
        assertThat(key.getName()).isEqualTo("id");
    }

    @Test
    public void getKey_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        entityManager.getKey((TestStringEntity) null);
    }

    @Test
    public void getKey_willThrowException_whenEntityHasNullId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        entityManager.getKey(new TestStringEntity(null));
    }

    @Test
    public void getKeyCollection() throws Exception {
        List<Key<TestStringEntity>> keys = entityManager.getKey(
                Arrays.asList(new TestStringEntityFixture().get(3))
        );
        assertThat(keys).containsExactly(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id3")
        );
    }

    @Test
    public void getKeyCollection_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        entityManager.getKey((Collection<TestStringEntity>) null);
    }

    @Test
    public void getKeyCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        entityManager.getKey(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id2")
                )
        );
    }

    @Test
    public void getKeyCollection_willThrowException_whenEntityHasNullId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        entityManager.getKey(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity(null),
                        new TestStringEntity("id2")
                )
        );
    }

    @Test
    public void getKeyVararg() throws Exception {
        List<Key<TestStringEntity>> keys = entityManager.getKey(
                new TestStringEntityFixture().get(3)
        );
        assertThat(keys).containsExactly(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id3")
        );
    }

    @Test
    public void getKeyVarargs_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        entityManager.getKey((TestStringEntity[]) null);
    }

    @Test
    public void getKeyVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        entityManager.getKey(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id2")
        );
    }

    @Test
    public void getKeyVarargs_willThrowException_whenEntityHasNullId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        entityManager.getKey(
                new TestStringEntity("id1"),
                new TestStringEntity(null),
                new TestStringEntity("id2")
        );
    }

    @Test
    public void toKeyMap() throws Exception {
        TestStringEntity a = new TestStringEntity("a");
        TestStringEntity b = new TestStringEntity("b");
        TestStringEntity c = new TestStringEntity("c");

        Map<Key<TestStringEntity>, TestStringEntity> result = entityManager.toKeyMap(
                Arrays.asList(a, b, c)
        );

        assertThat(result).hasSize(3);
        assertThat(result.get(Key.create(TestStringEntity.class, "a"))).isEqualTo(a);
        assertThat(result.get(Key.create(TestStringEntity.class, "b"))).isEqualTo(b);
        assertThat(result.get(Key.create(TestStringEntity.class, "c"))).isEqualTo(c);
    }

    @Test
    public void toKeyMap_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        entityManager.toKeyMap(null);
    }

    @Test
    public void getId() throws Exception {
        assertThat(entityManager.getId(new TestStringEntity("an-id"))).isEqualTo("an-id");
        assertThat(entityManager.getId(new TestStringEntity("another-id"))).isEqualTo("another-id");
        assertThat(entityManager.getId(new TestStringEntity(null))).isNull();
    }

    @Test
    public void getId_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No class 'java.lang.String' was registered");

        new TestNotAnEntityManager().getId("a string");
    }

    @Test
    public void getId_willWrapInIllegalArgumentException_whenIllegalAccessExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot get id for entity type TestStringEntity");

        Field field = TestStringEntity.class.getDeclaredField("id");
        field.setAccessible(false);

        TestEntityManager stringEntityManager = spy(new TestEntityManager());
        doReturn(field).when(stringEntityManager).getIdField();

        stringEntityManager.getId(new TestStringEntity("id"));
    }

    @Test
    public void getIdCollection() throws Exception {
        assertThat(entityManager.getId(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity("id2"),
                        new TestStringEntity("id3"),
                        new TestStringEntity(null)
                )
        )).contains(
                "id1",
                "id2",
                "id3",
                null
        );
    }

    @Test
    public void getIdCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        entityManager.getId(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null
                )
        );
    }

    @Test
    public void getIdCollection_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        
        entityManager.getId((Collection<TestStringEntity>) null);
    }

    @Test
    public void getIdVarargs() throws Exception {
        assertThat(entityManager.getId(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                new TestStringEntity("id3"),
                new TestStringEntity(null)
        )).contains(
                "id1",
                "id2",
                "id3",
                null
        );
    }

    @Test
    public void getIdVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        entityManager.getId(
                new TestStringEntity("id1"),
                null
        );
    }

    @Test
    public void getIdVarargs_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        entityManager.getId((TestStringEntity[]) null);
    }


    @Test
    public void hasId() throws Exception {
        assertThat(entityManager.hasId(new TestStringEntity("an-id"))).isTrue();
        assertThat(entityManager.hasNoId(new TestStringEntity("an-id"))).isFalse();
        assertThat(entityManager.hasId(new TestStringEntity(null))).isFalse();
        assertThat(entityManager.hasNoId(new TestStringEntity(null))).isTrue();

    }

    @Test
    public void hasId_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        assertThat(entityManager.hasId(null)).isFalse();
    }

    @Test
    public void hasNoId_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        assertThat(entityManager.hasNoId(null)).isFalse();
    }

    private class TestEntityManager implements EntityManager<TestStringEntity, String> {
        @Nonnull
        @Override
        public Class<TestStringEntity> getEntityType() {
            return TestStringEntity.class;
        }

        @Nonnull
        @Override
        public Class<String> getIdType() {
            return String.class;
        }

        @Override
        public Objectify ofy() {
            return ObjectifyService.ofy();
        }
    }

    private class TestNotAnEntityManager implements EntityManager<String, Long> {
        @Nonnull
        @Override
        public Class<String> getEntityType() {
            return String.class;
        }

        @Nonnull
        @Override
        public Class<Long> getIdType() {
            return Long.class;
        }

        @Override
        public Objectify ofy() {
            return ObjectifyService.ofy();
        }
    }
}