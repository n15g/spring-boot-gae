package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.SaveException;
import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.objectify.TestStringEntityFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class StringAsyncRepositoryTests extends ObjectifyTest {

    protected TestStringEntityFixture fixture = new TestStringEntityFixture();

    protected abstract AsyncRepository<TestStringEntity, String> getRepository();

    @Test
    public void saveAsync() throws Exception {
        TestStringEntity saved = getRepository().saveAsync(new TestStringEntity("my-id").setName("name")).get();

        TestStringEntity loaded = load("my-id");

        assertThat(loaded.getId()).isEqualTo(saved.getId());
        assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void saveAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        getRepository().saveAsync((TestStringEntity) null).get();
    }

    @Test
    public void saveAsync_willThrowException_whenInputHasNoId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().saveAsync(new TestStringEntity(null)).get();
    }

    @Test
    public void saveAsyncCollection() throws Exception {
        List<TestStringEntity> saved = getRepository().saveAsync(
                Arrays.asList(fixture.get(3))
        ).get();
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");
        getRepository().saveAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        ).get();
    }

    @Test
    public void saveAsyncCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().saveAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity(null),
                        new TestStringEntity("id3")
                )
        ).get();
    }

    @Test
    public void saveAsyncVarargs() throws Exception {
        List<TestStringEntity> saved = getRepository().saveAsync(
                fixture.get(3)
        ).get();
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().saveAsync(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        ).get();
    }

    @Test
    public void saveAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().saveAsync(
                new TestStringEntity("id1"),
                new TestStringEntity(null),
                new TestStringEntity("id3")
        ).get();
    }

    @Test
    public void deleteAsync() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().deleteAsync(beforeDelete).run();

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync((TestStringEntity) null).run();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(new TestStringEntity(null)).run();
    }

    @Test
    public void deleteAsyncCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteAsync(
                Arrays.asList(entities[0], entities[1])
        ).run();

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync(
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
        getRepository().deleteAsync(
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

        getRepository().deleteAsync(
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
        getRepository().deleteAsync(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                null
        ).run();
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(
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
        getRepository().deleteByKeyAsync(Key.create(TestStringEntity.class, "id2")).run();

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync((TestStringEntity) null).run();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(new TestStringEntity(null)).run();
    }

    @Test
    public void deleteByKeyAsyncCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKeyAsync(
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
        getRepository().deleteByKeyAsync(
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

        getRepository().deleteByKeyAsync(
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
        getRepository().deleteByKeyAsync(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                null
        ).run();
    }

    protected TestStringEntity load(String id) {
        return ofy().load().key(Key.create(TestStringEntity.class, id)).now();
    }

    protected void verifyTestEntityCollectionSaved() {
        SoftAssertions softly = new SoftAssertions();

        TestStringEntity loaded1 = load("id1");
        softly.assertThat(loaded1.getId()).isEqualTo("id1");
        softly.assertThat(loaded1.getName()).isEqualTo("entity1");

        TestStringEntity loaded2 = load("id2");
        softly.assertThat(loaded2.getId()).isEqualTo("id2");
        softly.assertThat(loaded2.getName()).isEqualTo("entity2");

        TestStringEntity loaded3 = load("id3");
        softly.assertThat(loaded3.getId()).isEqualTo("id3");
        softly.assertThat(loaded3.getName()).isEqualTo("entity3");

        softly.assertAll();
    }
}
