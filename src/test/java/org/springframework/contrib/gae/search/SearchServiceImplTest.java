package org.springframework.contrib.gae.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        searchService.index(entity);

        Index index = getIndex(TestSearchEntity.class);
        Document result = index.get("id1");

        assertThat(result.getFields("stringField")).extracting("text").contains("String value 1");
        assertThat(result.getFields("stringArrayField")).extracting("text").containsExactlyInAnyOrder("value1", "value2", "value3");
        assertThat(result.getFields("stringListField")).extracting("text").containsExactlyInAnyOrder("9", "8", "7");

        assertThat(result.getFields("longField")).extracting("number").contains(1234567890d);
    }

    @Test
    public void index_willDoNothing_whenEntityHasNoSearchFields() {
        searchService.index(new EmptyEntity(), "some-id");

        Index index = getIndex(EmptyEntity.class);
        assertThat(index.get("some-id")).isNull();
    }

    @Test
    public void indexMultiple() {
        TestSearchEntity entity1 = new TestSearchEntity("entity1").setStringField("value1");
        TestSearchEntity entity2 = new TestSearchEntity("entity2").setStringField("value2");
        TestSearchEntity entity3 = new TestSearchEntity("entity3").setStringField("value3");

        searchService.index(Arrays.asList(
                entity1, entity2, entity3
        ));

        Index index = getIndex(TestSearchEntity.class);
        assertThat(index.get("entity1").getFields("stringField")).extracting("text").containsExactly("value1");
        assertThat(index.get("entity2").getFields("stringField")).extracting("text").containsExactly("value2");
        assertThat(index.get("entity3").getFields("stringField")).extracting("text").containsExactly("value3");
    }

    @Test
    public void indexMultiple_willDoNothing_whenEntityHasNoSearchFields() {
        Map<String, EmptyEntity> entities = new HashMap<>();
        entities.put("id1", new EmptyEntity());
        entities.put("id2", new EmptyEntity());
        entities.put("id3", new EmptyEntity());

        searchService.index(entities);

        Index index = getIndex(EmptyEntity.class);
        assertThat(index.get("id1")).isNull();
        assertThat(index.get("id2")).isNull();
        assertThat(index.get("id3")).isNull();
    }

    @Test
    public void indexMultiple_willNotFail_whenMapIsEmpty() {
        searchService.index(new HashMap<>());
    }

    @Test
    public void unindex() {
        Index index = getIndex(TestSearchEntity.class);

        searchService.index(new TestSearchEntity("entity1"), new TestSearchEntity("entity2"));
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
        ));

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
        searchService.unindex(TestSearchEntity.class, Collections.emptyList());
    }

    @Test
    public void clear() {
        List<TestSearchEntity> entityList = IntStream.range(1, 201)
                .mapToObj(i -> new TestSearchEntity("entity" + i))
                .collect(Collectors.toList());
        searchService.index(entityList);

        entityList = IntStream.range(201, 401)
                .mapToObj(i -> new TestSearchEntity("entity" + i))
                .collect(Collectors.toList());
        searchService.index(entityList);


        Index index = getIndex(TestSearchEntity.class);
        assertThat(index.get("entity1")).isNotNull();
        assertThat(index.get("entity2")).isNotNull();
        assertThat(index.get("entity300")).isNotNull();

        searchService.clear(TestSearchEntity.class);
        assertThat(index.get("entity1")).isNull();
        assertThat(index.get("entity2")).isNull();
        assertThat(index.get("entity300")).isNull();
    }

    @Test
    public void clear_willNotImpactOtherIndexes() {
        TestSearchEntity entity1 = new TestSearchEntity("entity1");
        OtherEntity otherEntity = new OtherEntity();

        searchService.index(entity1);
        searchService.index(otherEntity, "otherEntity");

        Index index = getIndex(TestSearchEntity.class);
        assertThat(index.get("entity1")).isNotNull();

        Index otherIndex = getIndex(OtherEntity.class);
        assertThat(otherIndex.get("otherEntity")).isNotNull();

        searchService.clear(TestSearchEntity.class);
        assertThat(index.get("entity1")).isNull();
        assertThat(otherIndex.get("otherEntity")).isNotNull();
    }

    @SuppressWarnings("unused")
    private class OtherEntity {
        @SearchIndex
        private String someField;

        public String getSomeField() {
            return someField;
        }

        public OtherEntity setSomeField(String someField) {
            this.someField = someField;
            return this;
        }
    }

    private class EmptyEntity {

    }
}