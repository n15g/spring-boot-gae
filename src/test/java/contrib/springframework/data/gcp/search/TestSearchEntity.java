package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.GeoPoint;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

@Entity
@SuppressWarnings("unused")
public class TestSearchEntity {
    @Id
    @SearchId
    @BusinessKey
    private String id;
    @Index
    @SearchIndex
    private String stringField;
    @Index
    @SearchIndex
    private long longField;
    @SearchIndex
    private GeoPoint geoPointField;
    @SearchIndex
    private String[] stringArrayField;
    @SearchIndex
    private List<String> stringListField;

    private String unindexedValue;

    private TestSearchEntity() {
    }

    public TestSearchEntity(@Nullable String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TestSearchEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getStringField() {
        return stringField;
    }

    public TestSearchEntity setStringField(String stringField) {
        this.stringField = stringField;
        return this;
    }

    public long getLongField() {
        return longField;
    }

    public TestSearchEntity setLongField(long longField) {
        this.longField = longField;
        return this;
    }

    public GeoPoint getGeoPointField() {
        return geoPointField;
    }

    public TestSearchEntity setGeoPointField(GeoPoint geoPointField) {
        this.geoPointField = geoPointField;
        return this;
    }

    public String[] getStringArrayField() {
        return stringArrayField;
    }

    public TestSearchEntity setStringArrayField(String[] stringArrayField) {
        this.stringArrayField = stringArrayField;
        return this;
    }

    public List<String> getStringListField() {
        return stringListField;
    }

    public TestSearchEntity setStringListField(List<String> stringListField) {
        this.stringListField = stringListField;
        return this;
    }

    public String getUnindexedValue() {
        return unindexedValue;
    }

    public TestSearchEntity setUnindexedValue(String unindexedValue) {
        this.unindexedValue = unindexedValue;
        return this;
    }

    @SearchIndex
    public String stringMethod() {
        return "indexedMethodValue";
    }

    @SearchIndex
    public String[] stringArrayMethod() {
        return new String[]{"value1", "value2", "value3"};
    }

    @SearchIndex
    public List<String> stringListMethod() {
        return Arrays.asList("1", "2", "3");
    }


    public String unindexedMethod() {
        return "unindexedMethodValue";
    }

    @Override
    public boolean equals(Object o) {
        return BusinessIdentity.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }

    @Override
    public String toString() {
        return BusinessIdentity.toString(this);
    }
}
