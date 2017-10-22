package contrib.springframework.data.gcp.search.conversion.converter;

import com.google.appengine.api.search.GeoPoint;
import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.conversion.DefaultSearchConversionService;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSearchConversionServiceTest {
    GenericConversionService conversionService = new DefaultSearchConversionService();

    @Test
    public void convert_willConvertStringToItself() {
        assertThat(convert("String")).isEqualTo("String");
    }

    @Test
    public void addConverter_willOverrideDefaultConverters() {
        assertThat(convert(2L)).isEqualTo("2");
        conversionService.addConverter(new OverrideConverter());
        assertThat(convert(2L)).isEqualTo("OVERRIDDEN");
    }

    @Test
    public void convert_willHandleNullValues() {
        assertThat(convert(null)).isNull();
    }

    @Test
    public void convert_willHandleShortValues() {
        assertThat(convert((short) 1)).isEqualTo("1");
        assertThat(convert((short) -1000)).isEqualTo("-1000");
    }

    @Test
    public void convert_willHandleCollectionOfShortValues() {
        assertThat(convertCollection(
                Arrays.asList((short) 1, (short) -1000)
        )).isEqualTo(
                Arrays.asList("1", "-1000")
        );

        assertThat(convertCollection(
                new short[]{1, -1000}
        )).isEqualTo(
                Arrays.asList("1", "-1000")
        );
    }

    @Test
    public void convert_willHandleIntegerValues() {
        assertThat(convert(1)).isEqualTo("1");
        assertThat(convert(-1000)).isEqualTo("-1000");
    }

    @Test
    public void convert_willHandleCollectionOfIntegerValues() {
        assertThat(convertCollection(
                Arrays.asList(1, -1000)
        )).isEqualTo(
                Arrays.asList("1", "-1000")
        );

        assertThat(convertCollection(
                new int[]{1, -1000}
        )).isEqualTo(
                Arrays.asList("1", "-1000")
        );
    }

    @Test
    public void convert_willHandleLongValues() {
        assertThat(convert(1_000_000_000_000L)).isEqualTo("1000000000000");
        assertThat(convert(-9_999_999_999_999_999L)).isEqualTo("-9999999999999999");
    }

    @Test
    public void convert_willHandleCollectionOfLongValues() {
        assertThat(convertCollection(
                Arrays.asList(1_000_000_000_000L, -9_999_999_999_999_999L)
        )).isEqualTo(
                Arrays.asList("1000000000000", "-9999999999999999")
        );

        assertThat(convertCollection(
                new long[]{1_000_000_000_000L, -9_999_999_999_999_999L}
        )).isEqualTo(
                Arrays.asList("1000000000000", "-9999999999999999")
        );
    }

    @Test
    public void convert_willHandleFloatValues() {
        assertThat(convert(9.876543f)).isEqualTo("9.876543");
        assertThat(convert(-1.2345678f)).isEqualTo("-1.2345678");
    }

    @Test
    public void convert_willHandleCollectionOfFloatValues() {
        assertThat(convertCollection(
                Arrays.asList(9.876543f, -1.2345678f)
        )).isEqualTo(
                Arrays.asList("9.876543", "-1.2345678")
        );

        assertThat(convertCollection(
                new float[]{9.876543f, -1.2345678f}
        )).isEqualTo(
                Arrays.asList("9.876543", "-1.2345678")
        );
    }

    @Test
    public void convert_willHandleDoubleValues() {
        assertThat(convert(9.876543210987654d)).isEqualTo("9.876543210987654");
        assertThat(convert(-1.234567891234567d)).isEqualTo("-1.234567891234567");
    }

    @Test
    public void convert_willHandleCollectionOfDoubleValues() {
        assertThat(convertCollection(
                Arrays.asList(9.876543210987654d, -1.234567891234567d)
        )).isEqualTo(
                Arrays.asList("9.876543210987654", "-1.234567891234567")
        );

        assertThat(convertCollection(
                new double[]{9.876543210987654d, -1.234567891234567d}
        )).isEqualTo(
                Arrays.asList("9.876543210987654", "-1.234567891234567")
        );
    }

    @Test
    public void convert_willHandleBooleanValues() {
        assertThat(convert(true)).isEqualTo("true");
        assertThat(convert(false)).isEqualTo("false");
    }

    @Test
    public void convert_willHandleCollectionOfBooleanValues() {
        assertThat(convertCollection(
                Arrays.asList(true, false)
        )).isEqualTo(
                Arrays.asList("true", "false")
        );

        assertThat(convertCollection(
                new boolean[]{true, false}
        )).isEqualTo(
                Arrays.asList("true", "false")
        );
    }

    @Test
    public void convert_willHandleEnumValues() {
        assertThat(convert(IndexType.TEXT)).isEqualTo("TEXT");
        assertThat(convert(IndexType.GEOPOINT)).isEqualTo("GEOPOINT");
    }

    @Test
    public void convert_willHandleCollectionOfEnumValues() {
        assertThat(convertCollection(
                Arrays.asList(IndexType.TEXT, IndexType.GEOPOINT)
        )).isEqualTo(
                Arrays.asList("TEXT", "GEOPOINT")
        );

        assertThat(convertCollection(
                new IndexType[]{IndexType.TEXT, IndexType.GEOPOINT}
        )).isEqualTo(
                Arrays.asList("TEXT", "GEOPOINT")
        );
    }

    @Test
    public void convert_willHandleUuidValues() {
        UUID uuid = UUID.randomUUID();
        assertThat(convert(uuid)).isEqualTo(uuid.toString());
    }

    @Test
    public void convert_willHandleCollectionOfUuidValues() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        assertThat(convertCollection(
                Arrays.asList(uuid1, uuid2)
        )).isEqualTo(
                Arrays.asList(uuid1.toString(), uuid2.toString())
        );

        assertThat(convertCollection(
                new UUID[]{uuid1, uuid2}
        )).isEqualTo(
                Arrays.asList(uuid1.toString(), uuid2.toString())
        );
    }

    @Test
    public void convert_willHandleDateValues() {
        assertThat(convert(new Date(1234567890L))).isEqualTo("1970-01-15T06:56:07.89Z");
    }

    @Test
    public void convert_willHandleCollectionOfDateValues() {
        assertThat(convertCollection(
                Arrays.asList(new Date(1234567890L), new Date(9876543210L))
        )).isEqualTo(
                Arrays.asList("1970-01-15T06:56:07.89Z", "1970-04-25T07:29:03.21Z")
        );

        assertThat(convertCollection(
                new Date[]{new Date(1234567890L), new Date(9876543210L)}
        )).isEqualTo(
                Arrays.asList("1970-01-15T06:56:07.89Z", "1970-04-25T07:29:03.21Z")
        );
    }

    @Test
    public void convert_willHandleOffsetDateTimeValues() {
        OffsetDateTime time = OffsetDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.UTC);
        assertThat(convert(time)).isEqualTo("2017-10-11T17:24:36.123456789Z");
    }

    @Test
    public void convert_willHandleOffsetDateTimeValues_whenValueHasOffset() {
        OffsetDateTime timeWithOffset = OffsetDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.ofHours(10));
        assertThat(convert(timeWithOffset)).isEqualTo("2017-10-11T17:24:36.123456789+10:00");
    }

    @Test
    public void convert_willHandleCollectionOfOffsetDateTimeValues() {
        OffsetDateTime time1 = OffsetDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.UTC);
        OffsetDateTime time2 = OffsetDateTime.of(2017, 10, 15, 16, 28, 40, 123456789, ZoneOffset.UTC);

        assertThat(convertCollection(
                Arrays.asList(time1, time2)
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789Z", "2017-10-15T16:28:40.123456789Z")
        );

        assertThat(convertCollection(
                new OffsetDateTime[]{time1, time2}
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789Z", "2017-10-15T16:28:40.123456789Z")
        );
    }

    @Test
    public void convert_willHandleCollectionOfOffsetDateTimeValues_whenValuesHaveOffset() {
        OffsetDateTime time1 = OffsetDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.ofHours(10));
        OffsetDateTime time2 = OffsetDateTime.of(2017, 10, 15, 16, 28, 40, 123456789, ZoneOffset.ofHours(10));

        assertThat(convertCollection(
                Arrays.asList(time1, time2)
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789+10:00", "2017-10-15T16:28:40.123456789+10:00")
        );

        assertThat(convertCollection(
                new OffsetDateTime[]{time1, time2}
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789+10:00", "2017-10-15T16:28:40.123456789+10:00")
        );
    }

    @Test
    public void convert_willHandleZonedDateTimeValues() {
        ZonedDateTime time = ZonedDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.UTC);
        assertThat(convert(time)).isEqualTo("2017-10-11T17:24:36.123456789Z");
    }

    @Test
    public void convert_willHandleZonedDateTimeValues_whenValueHasTimeZone() {
        ZonedDateTime timeWithZone = ZonedDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneId.of("Australia/Sydney"));
        assertThat(convert(timeWithZone)).isEqualTo("2017-10-11T17:24:36.123456789+11:00[Australia/Sydney]");
    }

    @Test
    public void convert_willHandleCollectionOfZonedDateTimeValues() {
        ZonedDateTime time1 = ZonedDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneOffset.UTC);
        ZonedDateTime time2 = ZonedDateTime.of(2017, 10, 15, 16, 28, 40, 123456789, ZoneOffset.UTC);

        assertThat(convertCollection(
                Arrays.asList(time1, time2)
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789Z", "2017-10-15T16:28:40.123456789Z")
        );

        assertThat(convertCollection(
                new ZonedDateTime[]{time1, time2}
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789Z", "2017-10-15T16:28:40.123456789Z")
        );
    }

    @Test
    public void convert_willHandleCollectionOfZonedDateTimeValues_whenValuesHaveTimeZone() {
        ZonedDateTime time1 = ZonedDateTime.of(2017, 10, 11, 17, 24, 36, 123456789, ZoneId.of("Australia/Sydney"));
        ZonedDateTime time2 = ZonedDateTime.of(2017, 10, 15, 16, 28, 40, 123456789, ZoneId.of("Australia/Sydney"));

        assertThat(convertCollection(
                Arrays.asList(time1, time2)
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789+11:00[Australia/Sydney]", "2017-10-15T16:28:40.123456789+11:00[Australia/Sydney]")
        );

        assertThat(convertCollection(
                new ZonedDateTime[]{time1, time2}
        )).isEqualTo(
                Arrays.asList("2017-10-11T17:24:36.123456789+11:00[Australia/Sydney]", "2017-10-15T16:28:40.123456789+11:00[Australia/Sydney]")
        );
    }

    @Test
    public void convert_willHandleGeoPointValues() {
        assertThat(convert(new GeoPoint(1.3, 1.4))).isEqualTo("geopoint(1.3, 1.4)");
    }

    @Test
    public void convert_willHandleCollectionOfGeoPointValues() {
        assertThat(convertCollection(
                Arrays.asList(new GeoPoint(1.3, 1.4), new GeoPoint(1.23456, 9.87654))
        )).isEqualTo(
                Arrays.asList("geopoint(1.3, 1.4)", "geopoint(1.23456, 9.87654)")
        );

        assertThat(convertCollection(
                new GeoPoint[]{new GeoPoint(1.3, 1.4), new GeoPoint(1.23456, 9.87654)}
        )).isEqualTo(
                Arrays.asList("geopoint(1.3, 1.4)", "geopoint(1.23456, 9.87654)")
        );
    }

    private String convert(Object source) {
        return conversionService.convert(source, String.class);
    }

    @SuppressWarnings("unchecked")
    private List<String> convertCollection(Object source) {
        return (List<String>) conversionService.convert(source, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
    }

    private static class OverrideConverter implements Converter<Long, String> {

        @Override
        public String convert(Long source) {
            return "OVERRIDDEN";
        }
    }
}