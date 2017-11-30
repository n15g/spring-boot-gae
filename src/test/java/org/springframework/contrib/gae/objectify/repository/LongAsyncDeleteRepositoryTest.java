package org.springframework.contrib.gae.objectify.repository;

import com.googlecode.objectify.Key;
import org.springframework.contrib.gae.objectify.TestLongEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class LongAsyncDeleteRepositoryTest extends AbstractLongRepositoryTest {

    @Autowired
    private AsyncDeleteRepository<TestLongEntity, Long> repository;

    @Test
    public void deleteAsync() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestLongEntity beforeDelete = load(2L);
        Assertions.assertThat(beforeDelete).isNotNull();
        Assertions.assertThat(beforeDelete.getName()).isEqualTo("entity2");
        repository.deleteAsync(beforeDelete).run();

        TestLongEntity afterDelete = load(2L);
        Assertions.assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync((TestLongEntity) null).run();
    }

    @Test
    public void deleteAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(new TestLongEntity(null)).run();
    }

    @Test
    public void deleteAsyncCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listBeforeDelete).hasSize(3);

        repository.deleteAsync(
                Arrays.asList(entities[0], entities[1])
        ).run();

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listAfterDelete).hasSize(1);
        Assertions.assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync(
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
        repository.deleteAsync(
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

        repository.deleteAsync(
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
        repository.deleteAsync(
                new TestLongEntity(1L),
                new TestLongEntity(2L),
                null
        ).run();
    }

    @Test
    public void deleteAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(
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
        repository.deleteByKeyAsync(Key.create(TestLongEntity.class, 2L)).run();

        TestLongEntity afterDelete = load(2L);
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        repository.deleteAsync((TestLongEntity) null).run();
    }

    @Test
    public void deleteByKeyAsync_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        repository.deleteAsync(new TestLongEntity(null)).run();
    }

    @Test
    public void deleteByKeyAsyncCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        repository.deleteByKeyAsync(
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
        repository.deleteByKeyAsync(
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

        repository.deleteByKeyAsync(
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
        repository.deleteByKeyAsync(
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
