package org.springframework.contrib.gae.search.conversion.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Convert {@link ZonedDateTime} values to ISO-8601 format {@link String} values.
 */
public class ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convert(ZonedDateTime source) {
        return source.format(FORMATTER);
    }
}
