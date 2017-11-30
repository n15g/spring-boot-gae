package org.springframework.contrib.gae.objectify;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import org.springframework.contrib.gae.search.SearchIndex;

import javax.annotation.Nullable;

@Entity
public class TestLongEntity {
    @Id
    @BusinessKey
    private Long id;

    @Index
    @SearchIndex
    private String name;

    private TestLongEntity() {
    }

    public TestLongEntity(@Nullable Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TestLongEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return BusinessIdentity.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        return BusinessIdentity.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }
}
