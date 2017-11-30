package org.springframework.contrib.gae.objectify.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.LocalDate;

/**
 * Converts JSR-310 {@link LocalDate} fields to an ISO-8601 string representation for persistence in the datastore.
 * All values are converted to UTC for persistence to allow lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour.
 * Supports nanosecond precision.
 */
public class LocalDateStringTranslatorFactory extends ValueTranslatorFactory<LocalDate, String> {

    /**
     * Create a new instance.
     */
    public LocalDateStringTranslatorFactory() {
        super(LocalDate.class);
    }

    @Override
    protected ValueTranslator<LocalDate, String> createValueTranslator(TypeKey<LocalDate> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<LocalDate, String>(String.class) {
            @Override
            protected LocalDate loadValue(String value, LoadContext ctx, Path path) throws SkipException {
                return LocalDate.parse(value);
            }

            @Override
            protected String saveValue(LocalDate value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return value.toString();
            }
        };
    }
}