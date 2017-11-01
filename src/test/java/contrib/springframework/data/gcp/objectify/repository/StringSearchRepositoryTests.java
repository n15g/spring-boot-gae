package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.search.Operator;
import contrib.springframework.data.gcp.search.query.Query;
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
        TestStringEntity target = new TestStringEntity("id2").setName("name2");
        getRepository().save(
                new TestStringEntity("id1").setName("name1"),
                target,
                new TestStringEntity("id3").setName("name3")
        );

        Query<TestStringEntity> query = getRepository().search()
                .filter("name", Operator.EQ, "name2")
                .build();

        assertThat(getRepository().execute(query))
                .containsExactly(target);
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestStringEntity target = new TestStringEntity("id3").setName("target");

        getRepository().save(
                new TestStringEntity("id1").setName("name1"),
                new TestStringEntity("id2").setName("name2"),
                target
        );

        Query<TestStringEntity> preDeleteQuery = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        assertThat(getRepository().execute(preDeleteQuery))
                .containsExactly(target);

        getRepository().delete(target);
        Query<TestStringEntity> postDeleteQuery = getRepository().search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        assertThat(getRepository().execute(postDeleteQuery))
                .isEmpty();
    }
}
