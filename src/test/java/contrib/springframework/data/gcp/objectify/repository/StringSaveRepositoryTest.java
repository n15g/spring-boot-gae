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
public class StringSaveRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private SaveRepository<TestStringEntity, String> repository;

    @Test
    public void save() throws Exception {
        TestStringEntity saved = repository.save(new TestStringEntity("my-id").setName("name"));

        TestStringEntity loaded = load("my-id");

        assertThat(loaded.getId()).isEqualTo(saved.getId());
        assertThat(loaded.getName()).isEqualTo(saved.getName());
    }

    @Test
    public void save_willThrowException_whenInputIsNull() throws Exception {
        thrown.expect(NullPointerException.class);

        repository.save((TestStringEntity) null);
    }

    @Test
    public void save_willThrowException_whenInputHasNoId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        repository.save(new TestStringEntity(null));
    }

    @Test
    public void saveCollection() throws Exception {
        List<TestStringEntity> saved = repository.save(
                Arrays.asList(fixture.get(3))
        );
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveCollection_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        repository.save(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        new TestStringEntity(null),
                        new TestStringEntity("id3")
                )
        );
    }

    @Test
    public void saveVarargs() throws Exception {
        List<TestStringEntity> saved = repository.save(
                fixture.get(3)
        );
        assertThat(saved).hasSize(3);

        verifyTestEntityCollectionSaved();
    }

    @Test
    public void saveVarargs_willThrowException_whenInputContainsEntityWithoutId() throws Exception {
        thrown.expect(SaveException.class);
        thrown.expectMessage("Cannot save an entity with a null String @Id");

        repository.save(
                new TestStringEntity("id1"),
                new TestStringEntity(null),
                new TestStringEntity("id3")
        );
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
