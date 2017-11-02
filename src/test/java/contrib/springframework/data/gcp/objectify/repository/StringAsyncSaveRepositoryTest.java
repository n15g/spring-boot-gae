package contrib.springframework.data.gcp.objectify.repository;

import com.googlecode.objectify.SaveException;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class StringAsyncSaveRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private AsyncSaveRepository<TestStringEntity, String> repository;

    @Test
    public void saveAsync() throws Exception {
        TestStringEntity saved = repository.saveAsync(new TestStringEntity("my-id").setName("name")).get();

        TestStringEntity loaded = load("my-id");

        assertThat(loaded.getId()).isEqualTo(saved.getId());
        assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void saveAsync_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        repository.saveAsync((TestStringEntity) null).get();
    }

    @Test
    public void saveAsync_willThrowException_whenInputHasNoId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        repository.saveAsync(new TestStringEntity(null)).get();
    }

    @Test
    public void saveAsyncCollection() throws Exception {
        List<TestStringEntity> saved = repository.saveAsync(
                Arrays.asList(fixture.get(3))
        ).get();
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");
        repository.saveAsync(
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

        repository.saveAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity(null),
                        new TestStringEntity("id3")
                )
        ).get();
    }

    @Test
    public void saveAsyncVarargs() throws Exception {
        List<TestStringEntity> saved = repository.saveAsync(
                fixture.get(3)
        ).get();
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Attempted to save a null entity");

        repository.saveAsync(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        ).get();
    }

    @Test
    public void saveAsyncVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        repository.saveAsync(
                new TestStringEntity("id1"),
                new TestStringEntity(null),
                new TestStringEntity("id3")
        ).get();
    }

    @SuppressWarnings("Duplicates")
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
