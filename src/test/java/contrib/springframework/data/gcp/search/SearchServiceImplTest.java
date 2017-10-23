package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchServiceImplTest extends SearchTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchMetadata searchMetadata;

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

        Index index = SearchServiceFactory.getSearchService().getIndex(IndexSpec.newBuilder().setName(searchMetadata.getIndexName(TestSearchEntity.class)));
        Document result = index.get("id1");

        assertThat(result.getFields("stringField")).extracting("text").contains("String value 1");
        assertThat(result.getFields("stringArrayField")).extracting("text").containsExactlyInAnyOrder("value1", "value2", "value3");
        assertThat(result.getFields("stringListField")).extracting("text").containsExactlyInAnyOrder("9", "8", "7");

        assertThat(result.getFields("longField")).extracting("number").contains(1234567890d);
    }

}