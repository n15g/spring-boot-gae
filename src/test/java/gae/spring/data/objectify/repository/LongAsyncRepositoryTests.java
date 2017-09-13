package gae.spring.data.objectify.repository;

import com.googlecode.objectify.Key;
import gae.spring.data.objectify.TestLongEntityFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import gae.spring.data.objectify.ObjectifyTest;
import gae.spring.data.objectify.TestLongEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class LongAsyncRepositoryTests extends ObjectifyTest {

    protected TestLongEntityFixture fixture = new TestLongEntityFixture();

    protected abstract AsyncRepository<TestLongEntity, Long> getRepository();

    @Test
    public void saveAsync() throws Exception {
        TestLongEntity saved = getRepository().saveAsync(new TestLongEntity(1L).setName("name")).get();

        TestLongEntity loaded = load(1L);

        Assertions.assertThat(loaded.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void saveAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        getRepository().saveAsync((TestLongEntity) null).get();
    }

    @Test
    public void saveAsync_willGenerateLongId_whenInputHasNoId() throws Exception {
        TestLongEntity saved = getRepository().saveAsync(new TestLongEntity(null).setName("name")).get();

        TestLongEntity loaded = load(1L);

        Assertions.assertThat(loaded.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void saveAsyncCollection() throws Exception {
        List<TestLongEntity> saved = getRepository().saveAsync(
                Arrays.asList(fixture.get(3))
        ).get();
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().saveAsync(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        ).get();
    }

    @Test
    public void saveAsyncCollection_willGenerateLongIds_whenInputContainsEntityWithoutId() throws Exception {
        List<TestLongEntity> entities = Arrays.asList(
                new TestLongEntity(null).setName("entity1"),
                new TestLongEntity(null).setName("entity2"),
                new TestLongEntity(null).setName("entity3")
        );

        List<TestLongEntity> saved = getRepository().saveAsync(entities).get();
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncVarargs() throws Exception {

        List<TestLongEntity> saved = getRepository().saveAsync(
                new TestLongEntity(1L).setName("entity1"),
                new TestLongEntity(2L).setName("entity2"),
                new TestLongEntity(3L).setName("entity3")
        ).get();
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().saveAsync(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        ).get();
    }

    @Test
    public void saveAsyncVarargs_willGenerateLongIds_whenInputContainsEntityWithoutId() throws Exception {
        List<TestLongEntity> saved = getRepository().saveAsync(
                new TestLongEntity().setName("entity1"),
                new TestLongEntity().setName("entity2"),
                new TestLongEntity().setName("entity3")
        ).get();
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void deleteAsync() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestLongEntity beforeDelete = load(2L);
        Assertions.assertThat(beforeDelete).isNotNull();
        Assertions.assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().deleteAsync(beforeDelete).run();

        TestLongEntity afterDelete = load(2L);
        Assertions.assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync((TestLongEntity) null).run();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(new TestLongEntity()).run();
    }

    @Test
    public void deleteAsyncCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteAsync(
                Arrays.asList(entities[0], entities[1])
        ).run();

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listAfterDelete).hasSize(1);
        Assertions.assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync(
                Arrays.asList(
                        new TestLongEntity(1L),
                        new TestLongEntity(2L),
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
                        new TestLongEntity(1L),
                        new TestLongEntity(2L),
                        new TestLongEntity(null)
                )
        ).run();
    }

    @Test
    public void deleteAsyncVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteAsync(
                entities[0],
                entities[1]
        ).run();

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listAfterDelete).hasSize(1);
        Assertions.assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync(
                new TestLongEntity(1L),
                new TestLongEntity(2L),
                null
        ).run();
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(
                new TestLongEntity(1L),
                new TestLongEntity(2L),
                new TestLongEntity(null)
        ).run();
    }

    @Test
    public void deleteByKeyAsync() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestLongEntity beforeDelete = load(2L);
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().deleteByKeyAsync(Key.create(TestLongEntity.class, 2L)).run();

        TestLongEntity afterDelete = load(2L);
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteAsync((TestLongEntity) null).run();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().deleteAsync(new TestLongEntity()).run();
    }

    @Test
    public void deleteByKeyAsyncCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKeyAsync(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L)
                )
        ).run();

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteByKeyAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteByKeyAsync(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L),
                        null
                )
        ).run();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyAsyncVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKeyAsync(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L)
        ).run();

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByKeyAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteByKeyAsync(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L),
                null
        ).run();
    }

    public TestLongEntity load(long id) {
        return ofy().load().key(Key.create(TestLongEntity.class, id)).now();
    }

    protected void verifyTestEntityCollectionSaved() {
        SoftAssertions softly = new SoftAssertions();

        TestLongEntity loaded1 = load(1L);
        softly.assertThat(loaded1.getId()).isEqualTo(1L);
        softly.assertThat(loaded1.getName()).isEqualTo("entity1");

        TestLongEntity loaded2 = load(2L);
        softly.assertThat(loaded2.getId()).isEqualTo(2L);
        softly.assertThat(loaded2.getName()).isEqualTo("entity2");

        TestLongEntity loaded3 = load(3L);
        softly.assertThat(loaded3.getId()).isEqualTo(3L);
        softly.assertThat(loaded3.getName()).isEqualTo("entity3");

        softly.assertAll();
    }
}
