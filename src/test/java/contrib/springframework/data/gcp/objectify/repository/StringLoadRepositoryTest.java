package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class StringLoadRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private LoadRepository<TestStringEntity, String> repository;

    @Test
    public void findAll() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        assertThat(repository.findAll())
                .containsExactlyInAnyOrder(entities);
    }

    @Test
    public void findAll_willReturnEmptyList_whenThereAreNoEntities() throws Exception {
        assertThat(repository.findAll())
                .isEmpty();
    }

    @Test
    public void findAllWithCount() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAll(2);
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(entities[0], entities[1]);
        result.forEach(entity -> assertThat(entities).contains(entity));
    }

    @Test
    public void findAllWithCount_willReturnEmptyList_whenThereAreNoEntities() throws Exception {
        assertThat(repository.findAll(69))
                .isEmpty();
    }

    @Test
    public void findAllCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAll(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        Key.create(TestStringEntity.class, "id3")
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly(entities);
    }

    @Test
    public void findAllCollection_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        List<TestStringEntity> result = repository.findAll(
                Collections.emptyList()
        );

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void findAllCollection_willNotContainMissingEntities_whenKeyDoesNotExist() throws Exception {
        TestStringEntity[] entities = fixture.get(2);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAll(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        Key.create(TestStringEntity.class, "id100")
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(entities);
    }

    @Test
    public void findAllCollection_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.findAll((Collection<Key<TestStringEntity>>) null);
    }

    @Test
    public void findAllCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.findAll(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        null
                )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAll(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id3")
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly(entities);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        List<TestStringEntity> result = repository.findAll((Key<TestStringEntity>[]) new Key[]{});

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willNotContainMissingEntities_whenKeyDoesNotExist() throws Exception {
        TestStringEntity[] entities = fixture.get(2);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAll(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id100")
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(entities);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.findAll((Key<TestStringEntity>) null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.findAll(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                null
        );
    }

    @Test
    public void findAllByField() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Bob"))
                .containsExactlyInAnyOrder(entities[0], entities[1])
                .doesNotContain(entities[2]);
    }

    @Test
    public void findAllByField_willNotReturnMatches_whenCaseIsMismatched() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Bob");
        entities[1].setName("bob");
        entities[2].setName("BoB");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Bob"))
                .containsExactlyInAnyOrder(entities[0])
                .doesNotContain(entities[1], entities[2]);
    }

    @Test
    public void findAllByField_willThrowException_whenFieldIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        repository.findAllByField(null, "Bob");
    }

    @Test
    public void findAllByField_willHandleNullSearch() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName(null);
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", (Object) null))
                .contains(entities[0])
                .doesNotContain(entities[1], entities[2]);
    }

    @Test
    public void findAllByField_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Greg"))
                .isEmpty();
    }

    @Test
    public void findAllByField_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", 1L))
                .isEmpty();
    }

    @Test
    public void findAllByFieldCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", Arrays.asList("Bob", "Mark")))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldCollection_willReturnEmptyList_whenFieldIsNull() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Tabatha");
        entities[2].setName("Sue");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAllByField(null, Arrays.asList("Bob", "Tabatha"));

        assertThat(result).isEmpty();
    }

    @Test
    public void findAllByFieldCollection_willHandleNullSearch() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName(null);
        entities[1].setName(null);
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", Arrays.asList("Mark", null)))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldCollection_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", Arrays.asList("Greg", "Tabatha")))
                .isEmpty();
    }

    @Test
    public void findAllByFieldCollection_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", Arrays.asList(1L, 2L)))
                .isEmpty();
    }

    @Test
    public void findAllByFieldVarargs() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Bob", "Mark"))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldVarargs_willReturnEmptyList_whenFieldIsNull() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Tabatha");
        entities[2].setName("Sue");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        List<TestStringEntity> result = repository.findAllByField(null, "Bob", "Tabatha");

        assertThat(result).isEmpty();
    }

    @Test
    public void findAllByFieldVarargs_willHandleNullSearch() throws Exception {
        TestStringEntity[] entities = fixture.get(5);
        entities[0].setName(null);
        entities[1].setName(null);
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Mark", null))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldVarargs_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", "Greg", "Tabatha"))
                .isEmpty();
    }

    @Test
    public void findAllByFieldVarargs_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(repository.findAllByField("name", 1L, 2L))
                .isEmpty();
    }

    @Test
    public void findOne() throws Exception {
        TestStringEntity entity = new TestStringEntity("id").setName("the name");
        ofy().save().entity(entity).now();

        Optional<TestStringEntity> result = repository.findOne(Key.create(TestStringEntity.class, "id"));
        assertThat(result.get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "id")
                .hasFieldOrPropertyWithValue("name", "the name");
    }

    @Test
    public void findOne_willReturnEmptyOptional_whenKeyDoesNotExist() throws Exception {
        Optional<TestStringEntity> result = repository.findOne(Key.create(TestStringEntity.class, "bad-id"));

        assertThat(result.isPresent()).isEqualTo(false);
    }

    @Test
    public void findOne_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.findOne(null);
    }
}
