package gae.spring.data.objectify.repository;

import com.googlecode.objectify.Key;
import gae.spring.data.objectify.TestLongEntity;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class LongRepositoryTests extends LongAsyncRepositoryTests {

    @Override
    protected abstract Repository<TestLongEntity, Long> getRepository();

    @Test
    public void save() throws Exception {
        TestLongEntity saved = getRepository().save(new TestLongEntity(1L).setName("name"));

        TestLongEntity loaded = load(1L);

        Assertions.assertThat(loaded.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void save_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        getRepository().save((TestLongEntity) null);
    }

    @Test
    public void save_willGenerateLongId_whenInputHasNoId() throws Exception {
        TestLongEntity saved = getRepository().save(new TestLongEntity(null).setName("name"));

        TestLongEntity loaded = load(1L);

        Assertions.assertThat(loaded.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void saveCollection() throws Exception {
        List<TestLongEntity> saved = getRepository().save(
                Arrays.asList(fixture.get(3))
        );
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().save(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        );
    }

    @Test
    public void saveCollection_willGenerateLongIds_whenInputContainsEntityWithoutId() throws Exception {
        List<TestLongEntity> entities = Arrays.asList(
                new TestLongEntity(null).setName("entity1"),
                new TestLongEntity(null).setName("entity2"),
                new TestLongEntity(null).setName("entity3")
        );

        List<TestLongEntity> saved = getRepository().save(entities);
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveVarargs() throws Exception {

        List<TestLongEntity> saved = getRepository().save(
                new TestLongEntity(1L).setName("entity1"),
                new TestLongEntity(2L).setName("entity2"),
                new TestLongEntity(3L).setName("entity3")
        );
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().save(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        );
    }

    @Test
    public void saveVarargs_willGenerateLongIds_whenInputContainsEntityWithoutId() throws Exception {
        List<TestLongEntity> saved = getRepository().save(
                new TestLongEntity().setName("entity1"),
                new TestLongEntity().setName("entity2"),
                new TestLongEntity().setName("entity3")
        );
        Assertions.assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void findAll() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        assertThat(getRepository().findAll())
                .containsExactlyInAnyOrder(entities);
    }

    @Test
    public void findAll_willReturnEmptyList_whenThereAreNoEntities() throws Exception {
        assertThat(getRepository().findAll())
                .isEmpty();
    }

    @Test
    public void findAllWithCount() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> result = getRepository().findAll(2);
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(entities[0], entities[1]);
        result.forEach(entity -> assertThat(entities).contains(entity));
    }

    @Test
    public void findAllWithCount_willReturnEmptyList_whenThereAreNoEntities() throws Exception {
        assertThat(getRepository().findAll(69))
                .isEmpty();
    }

    @Test
    public void findAllCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L),
                        Key.create(TestLongEntity.class, 3L)
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsEntry(Key.create(TestLongEntity.class, 1L), Optional.of(entities[0]))
                .containsEntry(Key.create(TestLongEntity.class, 2L), Optional.of(entities[1]))
                .containsEntry(Key.create(TestLongEntity.class, 3L), Optional.of(entities[2]));
    }

    @Test
    public void findAllCollection_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll(
                Collections.emptyList()
        );

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void findAllCollection_willNotContainMissingEntities_whenKeyDoesNotExist() throws Exception {
        TestLongEntity[] entities = fixture.get(2);
        ofy().save().entities(entities).now();

        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L),
                        Key.create(TestLongEntity.class, 999L)
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsEntry(Key.create(TestLongEntity.class, 1L), Optional.of(entities[0]))
                .containsEntry(Key.create(TestLongEntity.class, 2L), Optional.of(entities[1]))
                .doesNotContainKey(Key.create(TestLongEntity.class, 999L));
    }

    @Test
    public void findAllCollection_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll((Collection<Key<TestLongEntity>>) null);
    }

    @Test
    public void findAllCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L),
                        null
                )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L),
                Key.create(TestLongEntity.class, 3L)
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsEntry(Key.create(TestLongEntity.class, 1L), Optional.of(entities[0]))
                .containsEntry(Key.create(TestLongEntity.class, 2L), Optional.of(entities[1]))
                .containsEntry(Key.create(TestLongEntity.class, 3L), Optional.of(entities[2]));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll((Key<TestLongEntity>[]) new Key[]{});

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willNotContainMissingEntities_whenKeyDoesNotExist() throws Exception {
        TestLongEntity[] entities = fixture.get(2);
        ofy().save().entities(entities).now();

        Map<Key<TestLongEntity>, Optional<TestLongEntity>> result = getRepository().findAll(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L),
                Key.create(TestLongEntity.class, 999L)
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsEntry(Key.create(TestLongEntity.class, 1L), Optional.of(entities[0]))
                .containsEntry(Key.create(TestLongEntity.class, 2L), Optional.of(entities[1]))
                .doesNotContainKey(Key.create(TestLongEntity.class, 999L));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll((Key<TestLongEntity>) null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L),
                null
        );
    }

    @Test
    public void findAllByField() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Bob"))
                .containsExactlyInAnyOrder(entities[0], entities[1])
                .doesNotContain(entities[2]);
    }

    @Test
    public void findAllByField_willNotReturnMatches_whenCaseIsMismatched() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Bob");
        entities[1].setName("bob");
        entities[2].setName("BoB");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Bob"))
                .containsExactlyInAnyOrder(entities[0])
                .doesNotContain(entities[1], entities[2]);
    }

    @Test
    public void findAllByField_willThrowException_whenFieldIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        getRepository().findAllByField(null, "Bob");
    }

    @Test
    public void findAllByField_willHandleNullSearch() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName(null);
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", (Object) null))
                .contains(entities[0])
                .doesNotContain(entities[1], entities[2]);
    }

    @Test
    public void findAllByField_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Greg"))
                .isEmpty();
    }

    @Test
    public void findAllByField_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", 1L))
                .isEmpty();
    }

    @Test
    public void findAllByFieldCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", Arrays.asList("Bob", "Mark")))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldCollection_willReturnEmptyList_whenFieldIsNull() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Tabatha");
        entities[2].setName("Sue");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();
        List<TestLongEntity> result = getRepository().findAllByField(null, Arrays.asList("Bob", "Tabatha"));

        assertThat(result).isEmpty();
    }


    @Test
    public void findAllByFieldCollection_willHandleNullSearch() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName(null);
        entities[1].setName(null);
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", Arrays.asList("Mark", null)))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldCollection_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", Arrays.asList("Greg", "Tabatha")))
                .isEmpty();
    }

    @Test
    public void findAllByFieldCollection_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", Arrays.asList(1L, 2L)))
                .isEmpty();
    }

    @Test
    public void findAllByFieldVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Bob");
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Bob", "Mark"))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldVarargs_willReturnEmptyList_whenFieldIsNull() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName("Bob");
        entities[1].setName("Tabatha");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        List<TestLongEntity> result = getRepository().findAllByField(null, "Bob", "Tabatha");
        assertThat(result.isEmpty());
    }


    @Test
    public void findAllByFieldVarargs_willHandleNullSearch() throws Exception {
        TestLongEntity[] entities = fixture.get(5);
        entities[0].setName(null);
        entities[1].setName(null);
        entities[2].setName("Sue");
        entities[3].setName("Mark");
        entities[4].setName("Greg");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Mark", null))
                .containsExactlyInAnyOrder(entities[0], entities[1], entities[3])
                .doesNotContain(entities[2], entities[4]);
    }

    @Test
    public void findAllByFieldVarargs_willReturnEmptyList_whenThereAreNoMatches() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Greg", "Tabatha"))
                .isEmpty();
    }

    @Test
    public void findAllByFieldVarargs_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", 1L, 2L))
                .isEmpty();
    }

    @Test
    public void findOne() throws Exception {
        TestLongEntity entity = new TestLongEntity(1L).setName("the name");
        ofy().save().entity(entity).now();

        Optional<TestLongEntity> result = getRepository().findOne(Key.create(TestLongEntity.class, 1L));
        assertThat(result.get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "the name");
    }

    @Test
    public void findOne_willReturnEmptyOptional_whenKeyDoesNotExist() throws Exception {
        Optional<TestLongEntity> result = getRepository().findOne(Key.create(TestLongEntity.class, 999L));

        assertThat(result.isPresent()).isEqualTo(false);
    }


    @Test
    public void findOne_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findOne(null);
    }

    @Test
    public void delete() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestLongEntity beforeDelete = load(2L);
        Assertions.assertThat(beforeDelete).isNotNull();
        Assertions.assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().delete(beforeDelete);

        TestLongEntity afterDelete = load(2L);
        Assertions.assertThat(afterDelete).isNull();
    }

    @Test
    public void delete_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete((TestLongEntity) null);
    }

    @Test
    public void delete_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(new TestLongEntity());
    }

    @Test
    public void deleteCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listBeforeDelete).hasSize(3);

        getRepository().delete(
                Arrays.asList(entities[0], entities[1])
        );

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listAfterDelete).hasSize(1);
        Assertions.assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete(
                Arrays.asList(
                        new TestLongEntity(1L),
                        new TestLongEntity(2L),
                        null
                )
        );
    }

    @Test
    public void deleteCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(
                Arrays.asList(
                        new TestLongEntity(1L),
                        new TestLongEntity(2L),
                        new TestLongEntity(null)
                )
        );
    }

    @Test
    public void deleteVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listBeforeDelete).hasSize(3);

        getRepository().delete(
                entities[0],
                entities[1]
        );

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        Assertions.assertThat(listAfterDelete).hasSize(1);
        Assertions.assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete(
                new TestLongEntity(1L),
                new TestLongEntity(2L),
                null
        );
    }

    @Test
    public void deleteVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(
                new TestLongEntity(1L),
                new TestLongEntity(2L),
                new TestLongEntity(null)
        );
    }

    @Test
    public void deleteByKey() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestLongEntity beforeDelete = load(2L);
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().deleteByKey(Key.create(TestLongEntity.class, 2L));

        TestLongEntity afterDelete = load(2L);
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete((TestLongEntity) null);
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(new TestLongEntity());
    }

    @Test
    public void deleteByKeyCollection() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKey(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L)
                )
        );

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteByKeyCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteByKey(
                Arrays.asList(
                        Key.create(TestLongEntity.class, 1L),
                        Key.create(TestLongEntity.class, 2L),
                        null
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deleteByKeyVarargs() throws Exception {
        TestLongEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestLongEntity> listBeforeDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKey(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L)
        );

        List<TestLongEntity> listAfterDelete = ofy().load().type(TestLongEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deleteByKeyVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().deleteByKey(
                Key.create(TestLongEntity.class, 1L),
                Key.create(TestLongEntity.class, 2L),
                null
        );
    }
}
