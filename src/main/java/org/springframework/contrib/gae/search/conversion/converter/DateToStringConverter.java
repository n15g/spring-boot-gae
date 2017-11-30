package org.springframework.contrib.gae.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Convert {@link Date} values to ISO-8601 format {@link String} values.
 */
public class DateToStringConverter implements Converter<Date, String> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneOffset.UTC);

    @Override
    public String convert(Date source) {
        return FORMATTER.format(source.toInstant());
    }
}
