package gae.spring.data.objectify;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import gae.spring.data.objectify.search.SearchIndex;

@Entity
public class TestStringEntity {
    @Id
    @BusinessKey
    private String id;

    @Index
    @SearchIndex
    private String name;

    public TestStringEntity() {
    }

    public TestStringEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TestStringEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestStringEntity setName(String name) {
        this.name = name;
        return this;
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
