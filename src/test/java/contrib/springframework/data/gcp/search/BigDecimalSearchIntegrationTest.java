package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static contrib.springframework.data.gcp.search.Operator.EQUAL;
import static contrib.springframework.data.gcp.search.Operator.GREATER_THAN;
import static contrib.springframework.data.gcp.search.Operator.GREATER_THAN_OR_EQUAL;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalSearchIntegrationTest extends SearchTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void findADocumentByExplicitMatch() {
        searchService.index(Arrays.asList(
                new TestEntity("id1", BigDecimal.ZERO),
                new TestEntity("id2", BigDecimal.ONE),
                new TestEntity("id3", BigDecimal.TEN)
        ));

        Results<ScoredDocument> results = searchService.search(TestEntity.class)
                .filter("value", EQUAL, BigDecimal.ONE)
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactly("id2");
    }

    @Test
    public void findADocumentByGreaterThanMatch() {
        searchService.index(Arrays.asList(
                new TestEntity("id1", valueOf(1)),
                new TestEntity("id2", valueOf(10)),
                new TestEntity("id3", valueOf(10.5)),
                new TestEntity("id4", valueOf(11))
        ));

        Results<ScoredDocument> results = searchService.search(TestEntity.class)
                .filter("value", GREATER_THAN, valueOf(10))
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id3", "id4");
    }

    @Test
    public void findADocumentByGreaterThanOrEqualMatch() {
        searchService.index(Arrays.asList(
                new TestEntity("id1", valueOf(1)),
                new TestEntity("id2", valueOf(10)),
                new TestEntity("id3", valueOf(10.5)),
                new TestEntity("id4", valueOf(11))
        ));

        Results<ScoredDocument> results = searchService.search(TestEntity.class)
                .filter("value", GREATER_THAN_OR_EQUAL, valueOf(10))
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id2", "id3", "id4");
    }

    @SuppressWarnings("unused")
    private static final class TestEntity {

        @SearchId
        private String id;

        @SearchIndex
        private BigDecimal value;

        public TestEntity(String id, BigDecimal value) {
            this.id = id;
            this.value = value;
        }
    }
}