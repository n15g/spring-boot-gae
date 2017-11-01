package contrib.springframework.data.gcp.search;

import contrib.springframework.data.gcp.search.query.Query;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static contrib.springframework.data.gcp.search.Operator.EQUAL;
import static contrib.springframework.data.gcp.search.Operator.GREATER_THAN;
import static contrib.springframework.data.gcp.search.Operator.GREATER_THAN_OR_EQUAL;
import static org.assertj.core.api.Assertions.assertThat;

public class LongSearchIntegrationTest extends SearchTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void findADocumentByExplicitMatch() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setLongField(1),
                new TestSearchEntity("id2").setLongField(2),
                new TestSearchEntity("id3").setLongField(3)
        ));

        Query<TestSearchEntity> query = searchService.createQuery(TestSearchEntity.class)
                .filter("longField", EQUAL, 2)
                .build();

        assertThat(searchService.execute(query))
                .extractingResultOf("getId")
                .containsExactly("id2");
    }

    @Test
    public void findADocumentByGreaterThanMatch() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setLongField(1),
                new TestSearchEntity("id2").setLongField(2),
                new TestSearchEntity("id3").setLongField(3),
                new TestSearchEntity("id4").setLongField(4)
        ));

        Query<TestSearchEntity> query = searchService.createQuery(TestSearchEntity.class)
                .filter("longField", GREATER_THAN, 2)
                .build();

        assertThat(searchService.execute(query))
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id3", "id4");
    }

    @Test
    public void findADocumentByGreaterThanOrEqualMatch() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setLongField(1),
                new TestSearchEntity("id2").setLongField(2),
                new TestSearchEntity("id3").setLongField(3),
                new TestSearchEntity("id4").setLongField(4)
        ));

        Query<TestSearchEntity> query = searchService.createQuery(TestSearchEntity.class)
                .filter("longField", GREATER_THAN_OR_EQUAL, 2)
                .build();

        assertThat(searchService.execute(query))
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id2", "id3", "id4");
    }
}