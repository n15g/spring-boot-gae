package org.springframework.contrib.gae.objectify.repository;

import org.springframework.contrib.gae.objectify.TestLongEntity;
import org.springframework.contrib.gae.search.Operator;
import org.springframework.contrib.gae.search.query.Query;
import org.springframework.contrib.gae.search.query.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class LongSearchRepositoryTest extends AbstractLongRepositoryTest {

    @Autowired
    private SearchRepository<TestLongEntity, Long> repository;

    @Test
    public void save_willIndexInSearchService() {
        TestLongEntity target = new TestLongEntity(2L).setName("name2");
        repository.save(
                new TestLongEntity(1L).setName("name1"),
                target,
                new TestLongEntity(3L).setName("name3")
        );

        Query<TestLongEntity> query = repository.search()
                .filter("name", Operator.EQ, "name2")
                .build();
        Result<TestLongEntity> result = repository.execute(query);

        assertThat(result).containsExactly(target);
    }

    @Test
    public void delete_willRemoveEntryFromSearchService() {
        TestLongEntity target = new TestLongEntity(3L).setName("target");

        repository.save(
                new TestLongEntity(1L).setName("name1"),
                new TestLongEntity(2L).setName("name2"),
                target
        );

        Query<TestLongEntity> preDeleteQuery = repository.search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        Result<TestLongEntity> preDeleteResult = repository.execute(preDeleteQuery);
        assertThat(preDeleteResult).containsExactly(target);

        repository.delete(target);
        Query<TestLongEntity> postDeleteQuery = repository.search()
                .filter("name", Operator.EQ, target.getName())
                .build();
        Result<TestLongEntity> postDeleteResult = repository.execute(postDeleteQuery);
        assertThat(postDeleteResult).isEmpty();
    }
}
