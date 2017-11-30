package org.springframework.contrib.gae.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateSerializationTest extends ObjectifyTest {

    @Before
    public void setUp() throws Exception {
        objectify.register(LocalDateEntity.class);
    }

    @Test
    public void saveAndLoadValue() {
        LocalDate value = LocalDate.of(1776, 7, 4);

        LocalDateEntity saved = new LocalDateEntity("id", value);
        objectify.ofy().save().entity(saved).now();

        LocalDateEntity loaded = objectify.ofy().load().key(Key.create(saved)).now();

        assertThat(loaded.getValue()).isEqualTo(value);
    }

    @Test
    public void save_willIndexCorrectly() {
        LocalDateEntity value1 = new LocalDateEntity("id1", LocalDate.of(1776, 7, 4));
        LocalDateEntity value2 = new LocalDateEntity("id2", LocalDate.of(2017, 1, 20));
        LocalDateEntity value3 = new LocalDateEntity("id3", LocalDate.of(2017, 1, 21));

        objectify.ofy().save().entities(value1, value2, value3).now();

        assertThat(objectify.ofy().load().type(LocalDateEntity.class).order("value").list())
                .containsExactly(value1, value2, value3);

        assertThat(objectify.ofy().load().type(LocalDateEntity.class).order("-value").list())
                .containsExactly(value3, value2, value1);
    }

    @Entity
    private static class LocalDateEntity {
        @Id
        @BusinessKey
        private String id;

        @Index
        private LocalDate value;

        public LocalDateEntity() {
        }

        public LocalDateEntity(String id, LocalDate value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public LocalDate getValue() {
            return value;
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
}