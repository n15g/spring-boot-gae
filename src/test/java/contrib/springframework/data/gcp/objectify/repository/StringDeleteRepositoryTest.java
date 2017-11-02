package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class StringDeleteRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private DeleteRepository<TestStringEntity, String> repository;

    @Test
    public void delete() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        repository.delete(beforeDelete);

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void delete_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.delete((TestStringEntity) null);
    }

    @Test
    public void delete_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.delete(new TestStringEntity(null));
    }

    @Test
    public void deleteCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.delete(
                Arrays.asList(entities[0], entities[1])
        );

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.delete(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity("id2"),
                        null
                )
        );
    }

    @Test
    public void deleteCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.delete(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity("id2"),
                        new TestStringEntity(null)
                )
        );
    }

    @Test
    public void deleteVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.delete(
                entities[0],
                entities[1]
        );

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.delete(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                null
        );
    }

    @Test
    public void deleteVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.delete(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                new TestStringEntity(null)
        );
    }

    @Test
    public void deleteByKey() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        repository.deleteByKey(Key.create(TestStringEntity.class, "id2"));

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.delete((TestStringEntity) null);
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.delete(new TestStringEntity(null));
    }

    @Test
    public void deleteByKeyCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteByKey(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2")
                )
        );

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteByKeyCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteByKey(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        null
                )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteByKey(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2")
        );

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteByKey(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                null
        );
    }
}
