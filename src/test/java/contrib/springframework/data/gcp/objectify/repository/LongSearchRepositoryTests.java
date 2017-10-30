package contrib.springframework.data.gcp.objectify.repository;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.googlecode.objectify.Key;
import contrib.springframework.data.gcp.objectify.TestLongEntity;
import contrib.springframework.data.gcp.search.Operator;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public abstract class LongSearchRepositoryTests extends LongRepositoryTests {

    @Override
    protected abstract SearchRepository<TestLongEntity, Long> getRepository();

    @Test
    @Override
    public void saveCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        );
    }

    @Test
    @Override
    public void saveVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().save(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        );
    }

    @Test
    @Override
    public void saveAsyncCollection_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                Arrays.asList(
                        new TestLongEntity(1L),
                        null,
                        new TestLongEntity(3L)
                )
        ).get();
    }

    @Test
    @Override
    public void saveAsyncVarargs_willThrowException_whenInputContainsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        getRepository().saveAsync(
                new TestLongEntity(1L),
                null,
                new TestLongEntity(3L)
        ).get();
    }

    @Test
    public void save_willIndexInSearchService() {
        getRepository().save(
                new TestLongEntity(1L).setName("name1"),
                new TestLongEntity(2L).setName("name2"),
                new TestLongEntity(3L).setName("name3")
        );

        Results<ScoredDocument> result = getRepository().search()
                .filter("name", Operator.EQ, "name2")
                .execute();

        assertThat(result).extractingResultOf("getId").containsExactly(Key.create(TestLongEntity.class, 2L).toWebSafeString());
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestLongEntity target = new TestLongEntity(3L).setName("target");

        getRepository().save(
                new TestLongEntity(1L).setName("name1"),
                new TestLongEntity(2L).setName("name2"),
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
