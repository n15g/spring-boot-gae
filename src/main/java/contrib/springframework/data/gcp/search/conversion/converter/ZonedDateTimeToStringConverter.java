package contrib.springframework.data.gcp.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;

/**
 * Convert {@link ZonedDateTime} values to ISO-8601 format {@link String} values.
 */
public class ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {
    @Override
    public String convert(ZonedDateTime source) {
        return source.toString();
    }
}
