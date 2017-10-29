package contrib.springframework.data.gcp.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Convert {@link OffsetDateTime} values to ISO-8601 format {@link String} values.
 */
public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convert(OffsetDateTime source) {
        return source.format(FORMATTER);
    }
}
