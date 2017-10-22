package contrib.springframework.data.gcp.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Convert {@link Date} values to ISO-8601 format {@link String} values.
 */
public class DateToStringConverter implements Converter<Date, String> {
    @Override
    public String convert(Date source) {
        return DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC).format(source.toInstant());
    }
}
