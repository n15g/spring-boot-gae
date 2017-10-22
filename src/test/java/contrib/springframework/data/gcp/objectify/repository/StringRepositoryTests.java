package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.SaveException;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class StringRepositoryTests extends StringAsyncRepositoryTests {

    @Override
    protected abstract Repository<TestStringEntity, String> getRepository();

    @Test
    public void save() throws Exception {
        TestStringEntity saved = getRepository().save(new TestStringEntity("my-id").setName("name"));

        TestStringEntity loaded = load("my-id");

        assertThat(loaded.getId()).isEqualTo(saved.getId());
        assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void save_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        getRepository().save((TestStringEntity) null);
    }

    @Test
    public void save_willThrowException_whenInputHasNoId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().save(new TestStringEntity(null));
    }

    @Test
    public void saveCollection() throws Exception {
        List<TestStringEntity> saved = getRepository().save(
                Arrays.asList(fixture.get(3))
        );
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().save(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        );
    }

    @Test
    public void saveCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().save(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity(null),
                        new TestStringEntity("id3")
                )
        );
    }

    @Test
    public void saveVarargs() throws Exception {
        List<TestStringEntity> saved = getRepository().save(
                fixture.get(3)
        );
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        getRepository().save(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        );
    }

    @Test
    public void saveVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        getRepository().save(
                new TestStringEntity("id1"),
                new TestStringEntity(null),
                new TestStringEntity("id3")
        );
    }

    @Test
    public void findAll() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
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
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> result = getRepository().findAll(2);
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
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        Key.create(TestStringEntity.class, "id3")
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsEntry(Key.create(TestStringEntity.class, "id1"), Optional.of(entities[0]))
                .containsEntry(Key.create(TestStringEntity.class, "id2"), Optional.of(entities[1]))
                .containsEntry(Key.create(TestStringEntity.class, "id3"), Optional.of(entities[2]));
    }

    @Test
    public void findAllCollection_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll(
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

        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll(
                Arrays.asList(
                        Key.create(TestStringEntity.class, "id1"),
                        Key.create(TestStringEntity.class, "id2"),
                        Key.create(TestStringEntity.class, "id100")
                )
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsEntry(Key.create(TestStringEntity.class, "id1"), Optional.of(entities[0]))
                .containsEntry(Key.create(TestStringEntity.class, "id2"), Optional.of(entities[1]))
                .doesNotContainKey(Key.create(TestStringEntity.class, "id100"));
    }

    @Test
    public void findAllCollection_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll((Collection<Key<TestStringEntity>>) null);
    }

    @Test
    public void findAllCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll(
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

        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id3")
        );

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsEntry(Key.create(TestStringEntity.class, "id1"), Optional.of(entities[0]))
                .containsEntry(Key.create(TestStringEntity.class, "id2"), Optional.of(entities[1]))
                .containsEntry(Key.create(TestStringEntity.class, "id3"), Optional.of(entities[2]));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willReturnEmpty_whenNoKeysArePassed() throws Exception {
        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll((Key<TestStringEntity>[]) new Key[]{});

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willNotContainMissingEntities_whenKeyDoesNotExist() throws Exception {
        TestStringEntity[] entities = fixture.get(2);
        ofy().save().entities(entities).now();

        Map<Key<TestStringEntity>, Optional<TestStringEntity>> result = getRepository().findAll(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                Key.create(TestStringEntity.class, "id100")
        );

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsEntry(Key.create(TestStringEntity.class, "id1"), Optional.of(entities[0]))
                .containsEntry(Key.create(TestStringEntity.class, "id2"), Optional.of(entities[1]))
                .doesNotContainKey(Key.create(TestStringEntity.class, "id100"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll((Key<TestStringEntity>) null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findAllVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findAll(
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

        assertThat(getRepository().findAllByField("name", "Bob"))
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
        TestStringEntity[] entities = fixture.get(3);
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
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", "Greg"))
                .isEmpty();
    }

    @Test
    public void findAllByField_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", 1L))
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

        assertThat(getRepository().findAllByField("name", Arrays.asList("Bob", "Mark")))
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

        List<TestStringEntity> result = getRepository().findAllByField(null, Arrays.asList("Bob", "Tabatha"));

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

        assertThat(getRepository().findAllByField("name", Arrays.asList("Mark", null)))
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

        assertThat(getRepository().findAllByField("name", Arrays.asList("Greg", "Tabatha")))
                .isEmpty();
    }

    @Test
    public void findAllByFieldCollection_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", Arrays.asList(1L, 2L)))
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

        assertThat(getRepository().findAllByField("name", "Bob", "Mark"))
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

        List<TestStringEntity> result = getRepository().findAllByField(null, "Bob", "Tabatha");

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

        assertThat(getRepository().findAllByField("name", "Mark", null))
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

        assertThat(getRepository().findAllByField("name", "Greg", "Tabatha"))
                .isEmpty();
    }

    @Test
    public void findAllByFieldVarargs_willNotFail_whenSearchTypeDoesNotMatchFieldType() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        entities[0].setName("Mark");
        entities[1].setName("Bob");
        entities[2].setName("Sue");

        ofy().save().entities(entities).now();

        assertThat(getRepository().findAllByField("name", 1L, 2L))
                .isEmpty();
    }

    @Test
    public void findOne() throws Exception {
        TestStringEntity entity = new TestStringEntity("id").setName("the name");
        ofy().save().entity(entity).now();

        Optional<TestStringEntity> result = getRepository().findOne(Key.create(TestStringEntity.class, "id"));
        assertThat(result.get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", "id")
                .hasFieldOrPropertyWithValue("name", "the name");
    }

    @Test
    public void findOne_willReturnEmptyOptional_whenKeyDoesNotExist() throws Exception {
        Optional<TestStringEntity> result = getRepository().findOne(Key.create(TestStringEntity.class, "bad-id"));

        assertThat(result.isPresent()).isEqualTo(false);
    }


    @Test
    public void findOne_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().findOne(null);
    }

    @Test
    public void delete() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        TestStringEntity beforeDelete = load("id2");
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("entity2");
        getRepository().delete(beforeDelete);

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void delete_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete((TestStringEntity) null);
    }

    @Test
    public void delete_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(new TestStringEntity(null));
    }

    @Test
    public void deleteCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().delete(
                Arrays.asList(entities[0], entities[1])
        );

        List<TestStringEntity> listAfterDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listAfterDelete).hasSize(1);
        assertThat(listAfterDelete).containsExactly(entities[2]);
    }

    @Test
    public void deleteCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete(
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
        getRepository().delete(
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

        getRepository().delete(
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
        getRepository().delete(
                new TestStringEntity("id1"),
                new TestStringEntity("id2"),
                null
        );
    }

    @Test
    public void deleteVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(
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
        getRepository().deleteByKey(Key.create(TestStringEntity.class, "id2"));

        TestStringEntity afterDelete = load("id2");
        assertThat(afterDelete).isNull();
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().delete((TestStringEntity) null);
    }

    @Test
    public void deleteByKey_willThrowException_whenInputIdIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You cannot create a Key for an object with a null @Id");
        getRepository().delete(new TestStringEntity(null));
    }

    @Test
    public void deleteByKeyCollection() throws Exception {
        TestStringEntity[] entities = fixture.get(3);
        ofy().save().entities(entities).now();

        List<TestStringEntity> listBeforeDelete = ofy().load().type(TestStringEntity.class).list();
        assertThat(listBeforeDelete).hasSize(3);

        getRepository().deleteByKey(
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
        getRepository().deleteByKey(
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

        getRepository().deleteByKey(
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
        getRepository().deleteByKey(
                Key.create(TestStringEntity.class, "id1"),
                Key.create(TestStringEntity.class, "id2"),
                null
        );
    }
}
