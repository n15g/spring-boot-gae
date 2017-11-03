package contrib.springframework.data.gcp.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class OffsetDateTimeSerializationTest extends ObjectifyTest {

    @Before
    public void setUp() throws Exception {
        objectify.register(OffsetDateTimeEntity.class);
    }

    @Test
    public void saveAndLoadValue() {
        OffsetDateTime value = OffsetDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.UTC);

        OffsetDateTimeEntity saved = new OffsetDateTimeEntity("id", value);
        objectify.ofy().save().entity(saved).now();

        OffsetDateTimeEntity loaded = objectify.ofy().load().key(Key.create(saved)).now();

        assertThat(loaded.getValue()).isEqualTo(value);
    }

    @Test
    public void saveAndLoad_willWorkWithOtherOffset() {
        OffsetDateTime savedValue = OffsetDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.ofHours(10));

        OffsetDateTimeEntity saved = new OffsetDateTimeEntity("id", savedValue);
        objectify.ofy().save().entity(saved).now();

        OffsetDateTimeEntity loaded = objectify.ofy().load().key(Key.create(saved)).now();

        OffsetDateTime loadedValue = loaded.getValue();
        assertThat(loadedValue).isEqualTo(savedValue);
    }

    @Test
    public void save_willIndexCorrectly() {
        OffsetDateTimeEntity value1 = new OffsetDateTimeEntity("id1", OffsetDateTime.of(1776, 7, 4, 12, 34, 20, 0, ZoneOffset.ofHours(-8)));
        OffsetDateTimeEntity value2 = new OffsetDateTimeEntity("id2", OffsetDateTime.of(2017, 1, 20, 9, 0, 0, 0, ZoneOffset.ofHours(-8)));
        OffsetDateTimeEntity value3 = new OffsetDateTimeEntity("id3", OffsetDateTime.of(2017, 1, 20, 9, 0, 1, 0, ZoneOffset.ofHours(-8)));

        objectify.ofy().save().entities(value1, value2, value3).now();

        assertThat(objectify.ofy().load().type(OffsetDateTimeEntity.class).order("value").list())
                .containsExactly(value1, value2, value3);

        assertThat(objectify.ofy().load().type(OffsetDateTimeEntity.class).order("-value").list())
                .containsExactly(value3, value2, value1);
    }

    @Entity
    private static class OffsetDateTimeEntity {
        @Id
        @BusinessKey
        private String id;

        @Index
        private OffsetDateTime value;

        public OffsetDateTimeEntity() {
        }

        public OffsetDateTimeEntity(String id, OffsetDateTime value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public OffsetDateTime getValue() {
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