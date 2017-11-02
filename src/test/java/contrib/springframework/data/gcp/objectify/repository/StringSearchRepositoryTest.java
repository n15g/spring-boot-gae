package contrib.springframework.data.gcp.objectify.repository;

import contrib.springframework.data.gcp.objectify.TestStringEntity;
import contrib.springframework.data.gcp.search.Operator;
import contrib.springframework.data.gcp.search.query.Query;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class StringSearchRepositoryTest extends AbstractStringRepositoryTest {

    @Autowired
    private SearchRepository<TestStringEntity, String> repository;

    @Test
    public void save_willIndexInSearchService() {
        TestStringEntity target = new TestStringEntity("id2").setName("name2");
        repository.save(
                new TestStringEntity("id1").setName("name1"),
                target,
                new TestStringEntity("id3").setName("name3")
        );

        Query<TestStringEntity> query = repository.search()
                .filter("name", Operator.EQ, "name2")
                .build();

        assertThat(repository.execute(query))
                .containsExactly(target);
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestStringEntity target = new TestStringEntity("id3").setName("target");

        repository.save(
                new TestStringEntity("id1").setName("name1"),
                new TestStringEntity("id2").setName("name2"),
                target
        );

        Query<TestStringEntity> preDeleteQuery = repository.search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        assertThat(repository.execute(preDeleteQuery))
                .containsExactly(target);

        repository.delete(target);
        Query<TestStringEntity> postDeleteQuery = repository.search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        assertThat(repository.execute(postDeleteQuery))
                .isEmpty();
    }
}
