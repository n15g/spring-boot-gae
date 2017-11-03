package contrib.springframework.data.gcp.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZonedDateTimeSerializationTest extends ObjectifyTest {

    @Before
    public void setUp() throws Exception {
        objectify.register(ZonedDateTimeEntity.class);
    }

    @Test
    public void saveAndLoadValue() {
        ZonedDateTime value = ZonedDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.UTC);

        ZonedDateTimeEntity saved = new ZonedDateTimeEntity("id", value);
        objectify.ofy().save().entity(saved).now();

        ZonedDateTimeEntity loaded = objectify.ofy().load().key(Key.create(saved)).now();

        assertThat(loaded.getValue()).isEqualTo(value);
    }

    @Test
    public void saveAndLoad_willWorkWithOtherZoned() {
        ZonedDateTime savedValue = ZonedDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.ofHours(10));

        ZonedDateTimeEntity saved = new ZonedDateTimeEntity("id", savedValue);
        objectify.ofy().save().entity(saved).now();

        ZonedDateTimeEntity loaded = objectify.ofy().load().key(Key.create(saved)).now();

        ZonedDateTime loadedValue = loaded.getValue();
        assertThat(loadedValue).isEqualTo(savedValue);
    }

    @Test
    public void save_willIndexCorrectly() {
        ZonedDateTimeEntity value1 = new ZonedDateTimeEntity("id1", ZonedDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.ofHours(-8)));
        ZonedDateTimeEntity value2 = new ZonedDateTimeEntity("id2", ZonedDateTime.of(2017, 1, 20, 9, 0, 0, 0, ZoneOffset.ofHours(-8)));
        ZonedDateTimeEntity value3 = new ZonedDateTimeEntity("id3", ZonedDateTime.of(2017, 1, 20, 9, 0, 1, 0, ZoneOffset.ofHours(-8)));

        objectify.ofy().save().entities(value1, value2, value3).now();

        assertThat(objectify.ofy().load().type(ZonedDateTimeEntity.class).order("value").list())
                .containsExactly(value1, value2, value3);

        assertThat(objectify.ofy().load().type(ZonedDateTimeEntity.class).order("-value").list())
                .containsExactly(value3, value2, value1);
    }

    @Entity
    private static class ZonedDateTimeEntity {
        @Id
        @BusinessKey
        private String id;

        @Index
        private ZonedDateTime value;

        public ZonedDateTimeEntity() {
        }

        public ZonedDateTimeEntity(String id, ZonedDateTime value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public ZonedDateTime getValue() {
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