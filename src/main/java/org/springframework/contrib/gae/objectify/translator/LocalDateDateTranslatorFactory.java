package org.springframework.contrib.gae.objectify.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;


/**
 * Converts JSR-310 {@link LocalDate} fields to {@link Date} values for persistence in the datastore.
 */
public class LocalDateDateTranslatorFactory extends ValueTranslatorFactory<LocalDate, Date> {

    /**
     * Create a new instance.
     */
    public LocalDateDateTranslatorFactory() {
        super(LocalDate.class);
    }

    @Override
    protected ValueTranslator<LocalDate, Date> createValueTranslator(TypeKey<LocalDate> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<LocalDate, Date>(Date.class) {
            @Override
            protected LocalDate loadValue(Date value, LoadContext ctx, Path path) throws SkipException {
                return Instant.ofEpochMilli(value.getTime()).atZone(ZoneOffset.UTC).toLocalDate();
            }

            @Override
            protected Date saveValue(LocalDate value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return Date.from(value.atStartOfDay(ZoneOffset.UTC).toInstant());
            }
        };
    }
}
