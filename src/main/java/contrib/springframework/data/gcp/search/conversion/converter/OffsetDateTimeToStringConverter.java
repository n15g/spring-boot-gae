package contrib.springframework.data.gcp.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;

/**
 * Convert {@link OffsetDateTime} values to ISO-8601 format {@link String} values.
 */
public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {
    @Override
    public String convert(OffsetDateTime source) {
        return source.toString();
    }
}
