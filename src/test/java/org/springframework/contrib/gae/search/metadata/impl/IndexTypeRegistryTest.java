package org.springframework.contrib.gae.search.metadata.impl;

import com.google.appengine.api.search.GeoPoint;
import com.openpojo.random.util.SomeEnum;
import org.assertj.core.api.Assertions;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.metadata.IndexTypeRegistry;
import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexTypeRegistryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private IndexTypeRegistry lookup = new DefaultIndexTypeRegistry();

    @Test
    public void addDefaultMappings() throws Exception {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(lookup.apply(boolean.class)).isEqualTo(IndexType.IDENTIFIER);
        softly.assertThat(lookup.apply(Boolean.class)).isEqualTo(IndexType.IDENTIFIER);
        softly.assertThat(lookup.apply(Enum.class)).isEqualTo(IndexType.IDENTIFIER);
        softly.assertThat(lookup.apply(UUID.class)).isEqualTo(IndexType.IDENTIFIER);
        softly.assertThat(lookup.apply(Short.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Integer.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(short.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(int.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Integer.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Number.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(long.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Long.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(float.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Float.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(double.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(Double.class)).isEqualTo(IndexType.NUMBER);
        softly.assertThat(lookup.apply(CharSequence.class)).isEqualTo(IndexType.TEXT);
        softly.assertThat(lookup.apply(OffsetDateTime.class)).isEqualTo(IndexType.DATE);
        softly.assertThat(lookup.apply(ZonedDateTime.class)).isEqualTo(IndexType.DATE);
        softly.assertThat(lookup.apply(Date.class)).isEqualTo(IndexType.DATE);
        softly.assertThat(lookup.apply(GeoPoint.class)).isEqualTo(IndexType.GEOPOINT);

        softly.assertAll();
    }

    @Test
    public void apply_willReturnCorrectType_whenPassedASubclassOfAKnownType() throws Exception {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(lookup.apply(String.class)).isEqualTo(IndexType.TEXT);
        softly.assertThat(lookup.apply(SomeEnum.class)).isEqualTo(IndexType.IDENTIFIER);

        softly.assertAll();
    }

    @Test
    public void applyCollectionTypes() throws Exception {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(lookup.apply(
                CollectionFields.class.getDeclaredField("stringList").getGenericType())
        ).isEqualTo(IndexType.TEXT);

        softly.assertThat(lookup.apply(
                CollectionFields.class.getDeclaredField("integerSet").getGenericType())
        ).isEqualTo(IndexType.NUMBER);

        softly.assertThat(lookup.apply(
                CollectionFields.class.getDeclaredField("stringArray").getGenericType())
        ).isEqualTo(IndexType.TEXT);

        softly.assertThat(lookup.apply(
                CollectionFields.class.getDeclaredField("intArray").getGenericType())
        ).isEqualTo(IndexType.NUMBER);

        softly.assertAll();
    }

    @Test
    public void addMapping() throws Exception {
        lookup.addMapping(IndexTypeRegistryTest.class, IndexType.GEOPOINT);

        Assertions.assertThat(lookup.apply(IndexTypeRegistryTest.class)).isEqualTo(IndexType.GEOPOINT);
    }

    @Test
    public void apply_willThrowException_whenTypeIsUnknown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unknown type:");
        lookup.apply(IndexTypeRegistryTest.class);
    }

    private class CollectionFields {
        List<String> stringList;
        Set<Integer> integerSet;

        String[] stringArray;
        int[] intArray;
    }
}