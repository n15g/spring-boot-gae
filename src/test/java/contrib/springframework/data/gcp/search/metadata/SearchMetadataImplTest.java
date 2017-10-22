package contrib.springframework.data.gcp.search.metadata;

import com.google.appengine.api.search.GeoPoint;
import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.SearchIndex;
import contrib.springframework.data.gcp.search.TestSearchEntity;
import contrib.springframework.data.gcp.search.metadata.impl.SearchMetadataImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SearchMetadataImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private IndexTypeRegistry indexTypeRegistry;

    private SearchMetadata searchMetadata;

    @Before
    public void setUp() throws Exception {
        searchMetadata = Mockito.spy(new SearchMetadataImpl(
                indexTypeRegistry
        ));
    }

    @Test
    public void getId() {
        TestSearchEntity entity = new TestSearchEntity("id");
        assertThat((String) searchMetadata.getId(entity)).isEqualTo("id");
    }

    @Test
    public void getId_willReturnNull_whenIdIsNull() {
        TestSearchEntity entity = new TestSearchEntity(null);
        assertThat((String) searchMetadata.getId(entity)).isEqualTo(null);
    }

    @Test
    public void getId_willThrowException_whenEntityHasNoSearchId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No @SearchId on entity class: " + TestEntityWithBadFieldNames.class);
        assertThat((String) searchMetadata.getId(new TestEntityWithBadFieldNames()));
    }

    @Test
    public void getSearchFieldValues() throws Exception {
        TestSearchEntity entity = new TestSearchEntity("id")
                .setStringField("stringValue")
                .setLongField(1234567890L)
                .setGeoPointField(new GeoPoint(1.1, 2.2))
                .setStringArrayField(new String[]{"one", "two", "three"})
                .setLongListField(Arrays.asList(10L, 9L, 8L))
                .setUnindexedValue("unindexedValue");

        Map<String, Accessor> result = searchMetadata.getSearchFields(entity);
        assertThat(result.get("stringField").getValue(entity)).isEqualTo("stringValue");
        assertThat(result.get("stringField").getValue(entity)).isEqualTo("stringValue");
        assertThat(result.get("longField").getValue(entity)).isEqualTo(1234567890L);
        assertThat(result.get("geoPointField").getValue(entity)).isEqualTo(entity.getGeoPointField());
        assertThat(result.get("stringArrayField").getValue(entity)).isEqualTo(new String[]{"one", "two", "three"});
        assertThat(result.get("longListField").getValue(entity)).isEqualTo(Arrays.asList(10L, 9L, 8L));
        assertThat(result.get("stringMethod").getValue(entity)).isEqualTo("indexedMethodValue");
        assertThat(result.get("stringArrayMethod").getValue(entity)).isEqualTo(new String[]{"value1", "value2", "value3"});
        assertThat(result.get("longListMethod").getValue(entity)).isEqualTo(Arrays.asList(1L, 2L, 3L));
        assertThat(result).doesNotContainKeys("unindexedValue", "unindexedMethod");
    }

    @Test
    public void encodeFieldName() throws Exception {
        assertThat(searchMetadata.encodeFieldName(TestEntityWithBadFieldNames.class, "_fieldNameRequiringEncoding")).isEqualTo("fieldNameRequiringEncoding");
        assertThat(searchMetadata.encodeFieldName(TestEntityWithBadFieldNames.class, "explicitlyNamedFieldRequiringEncoding")).isEqualTo("explicitly_named_field_requiring_encoding");
    }

    @Test
    public void decodeFieldName() throws Exception {
        assertThat(searchMetadata.decodeFieldName(TestEntityWithBadFieldNames.class, "fieldNameRequiringEncoding")).isEqualTo("_fieldNameRequiringEncoding");
        assertThat(searchMetadata.decodeFieldName(TestEntityWithBadFieldNames.class, "explicitly_named_field_requiring_encoding")).isEqualTo("explicitlyNamedFieldRequiringEncoding");
    }

    @Test
    public void getFieldType() throws Exception {
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "stringField")).isEqualTo(String.class);
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "longField")).isEqualTo(long.class);
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "stringArrayField")).isEqualTo(String[].class);
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "longListField"))
                .isEqualTo(TestSearchEntity.class.getDeclaredField("longListField").getGenericType());
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "stringMethod")).isEqualTo(String.class);
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "stringArrayMethod")).isEqualTo(String[].class);
        assertThat(searchMetadata.getFieldType(TestSearchEntity.class, "longListMethod"))
                .isEqualTo(TestSearchEntity.class.getDeclaredMethod("longListMethod").getGenericReturnType());
    }

    @Test
    public void getIndexType() throws Exception {
        when(indexTypeRegistry.apply(String.class)).thenReturn(IndexType.GEOPOINT);
        assertThat(searchMetadata.getIndexType(TestSearchEntity.class, "stringField")).isEqualTo(IndexType.GEOPOINT);
    }

    @Test
    public void hasIndexedFields() throws Exception {
        assertThat(searchMetadata.hasIndexedFields(TestSearchEntity.class)).isTrue();
        assertThat(searchMetadata.hasIndexedFields(String.class)).isFalse();
    }

    @SuppressWarnings("unused")
    private static class TestEntityWithBadFieldNames {
        @SearchIndex
        private String _fieldNameRequiringEncoding;
        @SearchIndex("explicitly-named-field-requiring-encoding")
        private String explicitlyNamedFieldRequiringEncoding;
    }
}