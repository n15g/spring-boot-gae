package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchServiceImplTest extends SearchTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void index() {
        TestSearchEntity entity = new TestSearchEntity("id1")
                .setStringField("String value 1")
                .setLongField(1234567890L)
                .setStringArrayField(new String[]{"value1", "value2", "value3"})
                .setStringListField(Arrays.asList("9", "8", "7"))
                .setGeoPointField(new GeoPoint(1, 2))
                .setUnindexedValue("unindexed");

        searchService.index(entity).run();

        Index index = getIndex(TestSearchEntity.class);
        Document result = index.get("id1");

        assertThat(result.getFields("stringField")).extracting("text").contains("String value 1");
        assertThat(result.getFields("stringArrayField")).extracting("text").containsExactlyInAnyOrder("value1", "value2", "value3");
        assertThat(result.getFields("stringListField")).extracting("text").containsExactlyInAnyOrder("9", "8", "7");

        assertThat(result.getFields("longField")).extracting("number").contains(1234567890d);
    }

    @Test
    public void indexMultiple() {
        TestSearchEntity entity1 = new TestSearchEntity("entity1").setStringField("value1");
        TestSearchEntity entity2 = new TestSearchEntity("entity2").setStringField("value2");
        TestSearchEntity entity3 = new TestSearchEntity("entity3").setStringField("value3");

        searchService.index(Arrays.asList(
                entity1, entity2, entity3
        )).run();

        Index index = getIndex(TestSearchEntity.class);
        assertThat(index.get("entity1").getFields("stringField")).extracting("text").containsExactly("value1");
        assertThat(index.get("entity2").getFields("stringField")).extracting("text").containsExactly("value2");
        assertThat(index.get("entity3").getFields("stringField")).extracting("text").containsExactly("value3");
    }

    @Test
    public void indexMultiple_willNotFail_whenMapIsEmpty() {
        searchService.index(new HashMap<>()).run();
    }

    @Test
    public void unindex() {
        Index index = getIndex(TestSearchEntity.class);

        searchService.index(new TestSearchEntity("entity1"), new TestSearchEntity("entity2")).run();
        assertThat(index.get("entity1")).isNotNull();
        assertThat(index.get("entity2")).isNotNull();

        searchService.unindex(TestSearchEntity.class, "entity1");
        assertThat(index.get("entity1")).isNull();
        assertThat(index.get("entity2")).isNotNull();
    }

    @Test
    public void unindexMultiple() {
        TestSearchEntity entity1 = new TestSearchEntity("entity1");
        TestSearchEntity entity2 = new TestSearchEntity("entity2");
        TestSearchEntity entity3 = new TestSearchEntity("entity3");

        searchService.index(Arrays.asList(
                entity1, entity2, entity3
        )).run();

        Index index = getIndex(TestSearchEntity.class);
        assertThat(index.get("entity1")).isNotNull();
        assertThat(index.get("entity2")).isNotNull();
        assertThat(index.get("entity3")).isNotNull();

        searchService.unindex(TestSearchEntity.class, "entity1", "entity2");
        assertThat(index.get("entity1")).isNull();
        assertThat(index.get("entity2")).isNull();
        assertThat(index.get("entity3")).isNotNull();
    }

    @Test
    public void unindexMultiple_willNotFail_whenMapIsEmpty() {
        searchService.index(new HashMap<>()).run();
    }
}