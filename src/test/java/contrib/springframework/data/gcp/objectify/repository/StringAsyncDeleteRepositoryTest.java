package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class StringAsyncDeleteRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private AsyncDeleteRepository<TestStringEntity, String> repository;

    @Test
    public void deleteAsync() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        repository.deleteAsync(beforeDelete).run();

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync((TestStringEntity) null).run();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(new TestStringEntity(null)).run();
    }

    @Test
    public void deleteAsyncCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteAsync(
                Arrays.asList(entities[0], entities[1])
        ).run();

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity("id2"),
                        null
                )
        ).run();
    }

    @Test
    public void deleteAsyncCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity("id2"),
                        new TestStringEntity(null)
                )
        ).run();
    }

    @Test
    public void deleteAsyncVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteAsync(
                entities[0],
                entities[1]
        ).run();

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                null
        ).run();
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                new TestStringEntity(null)
        ).run();
    }

    @Test
    public void deleteByKeyAsync() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        repository.deleteByKeyAsync(Key.create(TestStringEntity.class, "id2")).run();

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync((TestStringEntity) null).run();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(new TestStringEntity(null)).run();
    }

    @Test
    public void deleteByKeyAsyncCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteByKeyAsync(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2")
                )
        ).run();

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteByKeyAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteByKeyAsync(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        null
                )
        ).run();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyAsyncVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteByKeyAsync(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2")
        ).run();

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteByKeyAsync(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                null
        ).run();
    }
}
