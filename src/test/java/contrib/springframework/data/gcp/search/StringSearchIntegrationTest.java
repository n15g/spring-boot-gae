package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static contrib.springframework.data.gcp.search.Operator.EQUAL;
import static org.assertj.core.api.Assertions.assertThat;

public class StringSearchIntegrationTest extends SearchTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void findADocumentByExplicitMatch() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setStringField("string1"),
                new TestSearchEntity("id2").setStringField("string2"),
                new TestSearchEntity("id3").setStringField("string3")
        ));

        Results<ScoredDocument> results = searchService.search(TestSearchEntity.class)
                .filter("stringField", EQUAL, "string2")
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactly("id2");
    }

    @Test
    public void findADocumentByValueInArray() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setStringArrayField(new String[]{"alpha", "bravo", "charlie"}),
                new TestSearchEntity("id2").setStringArrayField(new String[]{"delta", "echo", "foxtrot"}),
                new TestSearchEntity("id3").setStringArrayField(new String[]{"golf", "hotel", "india"}),
                new TestSearchEntity("id4").setStringArrayField(new String[]{"juliet", "kilo", "lima"})
        ));

        Results<ScoredDocument> results = searchService.search(TestSearchEntity.class)
                .filterIn("stringArrayField", EQUAL, "bravo", "foxtrot", "juliet")
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id1", "id2", "id4");
    }

    @Test
    public void findADocumentByValueInCollection() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setStringListField(Arrays.asList("alpha", "bravo", "charlie")),
                new TestSearchEntity("id2").setStringListField(Arrays.asList("delta", "echo", "foxtrot")),
                new TestSearchEntity("id3").setStringListField(Arrays.asList("golf", "hotel", "india")),
                new TestSearchEntity("id4").setStringListField(Arrays.asList("juliet", "kilo", "lima"))
        ));

        Results<ScoredDocument> results = searchService.search(TestSearchEntity.class)
                .filterIn("stringListField", EQUAL, "bravo", "foxtrot", "juliet")
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id1", "id2", "id4");
    }

    @Test
    public void findADocumentByMatchInAnyField() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setStringField("match").setStringListField(Arrays.asList("no", "nope", "hell no")),
                new TestSearchEntity("id2").setStringField("uh uh").setStringListField(Arrays.asList("nope", "match", "no-way")),
                new TestSearchEntity("id3").setStringField("hecka no").setStringListField(Arrays.asList("not here", "or here", "nor here"))
        ));

        Results<ScoredDocument> results = searchService.search(TestSearchEntity.class)
                .filter("match")
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id1", "id2");
    }

    @Test
    public void findADocumentByMatchInAnyField_willNotFail_whenInputContainsNulls() {
        searchService.index(Arrays.asList(
                new TestSearchEntity("id1").setStringField("match").setStringListField(Arrays.asList("no", "nope", "hell no")),
                new TestSearchEntity("id2").setStringField(null).setStringListField(Arrays.asList(null, "match", "no-way")),
                new TestSearchEntity("id3").setStringField("match").setStringListField(Arrays.asList(null, null, null)),
                new TestSearchEntity("id4").setStringField(null).setStringListField(null)
        ));

        Results<ScoredDocument> results = searchService.search(TestSearchEntity.class)
                .filter("match")
                .execute();

        assertThat(results)
                .extractingResultOf("getId")
                .containsExactlyInAnyOrder("id1", "id2", "id3");
    }
}