package contrib.springframework.data.gcp.objectify.repository;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.search.Operator;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class StringSearchRepositoryTests extends StringRepositoryTests {

    @Override
    protected abstract SearchRepository<TestStringEntity, String> getRepository();

    @Test
    @Override
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        );
    }

    @Test
    @Override
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        );
    }

    @Test
    @Override
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                Arrays.asList(
                        new TestStringEntity("id1"),
                        null,
                        new TestStringEntity("id3")
                )
        ).get();
    }

    @Test
    @Override
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                new TestStringEntity("id1"),
                null,
                new TestStringEntity("id3")
        ).get();
    }

    @Test
    public void save_willIndexInSearchService() {
        getRepository().save(
                new TestStringEntity("id1").setName("name1"),
                new TestStringEntity("id2").setName("name2"),
                new TestStringEntity("id3").setName("name3")
        );

        Results<ScoredDocument> result = getRepository().search()
                .filter("name", Operator.EQ, "name2")
                .execute();

        assertThat(result).extractingResultOf("getId").containsExactly(Key.create(TestStringEntity.class, "id2").toWebSafeString());
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestStringEntity target = new TestStringEntity("id3").setName("target");

        getRepository().save(
                new TestStringEntity("id1").setName("name1"),
                new TestStringEntity("id2").setName("name2"),
                target
        );

        Results<ScoredDocument> preDelete = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .execute();
        assertThat(preDelete).extractingResultOf("getId").containsExactly(Key.create(target).toWebSafeString());

        getRepository().delete(target);
        Results<ScoredDocument> postDelete = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .execute();
        assertThat(postDelete).extractingResultOf("getId").isEmpty();
    }
}
