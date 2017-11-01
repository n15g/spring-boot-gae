package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestLongEntity;
import contrib.springframework.data.gcp.search.Operator;
import contrib.springframework.data.gcp.search.query.Query;
import contrib.springframework.data.gcp.search.query.Result;
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
        TestLongEntity target = new TestLongEntity(2L).setName("name2");
        getRepository().save(
                new TestLongEntity(1L).setName("name1"),
                target,
                new TestLongEntity(3L).setName("name3")
        );

        Query<TestLongEntity> query = getRepository().search()
                .filter("name", Operator.EQ, "name2")
                .build();
        Result<TestLongEntity> result = getRepository().execute(query);

        assertThat(result).containsExactly(target);
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestLongEntity target = new TestLongEntity(3L).setName("target");

        getRepository().save(
                new TestLongEntity(1L).setName("name1"),
                new TestLongEntity(2L).setName("name2"),
                target
        );

        Query<TestLongEntity> preDeleteQuery = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        Result<TestLongEntity> preDeleteResult = getRepository().execute(preDeleteQuery);
        assertThat(preDeleteResult).containsExactly(target);

        getRepository().delete(target);
        Query<TestLongEntity> postDeleteQuery = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        Result<TestLongEntity> postDeleteResult = getRepository().execute(postDeleteQuery);
        assertThat(postDeleteResult).isEmpty();
    }
}
