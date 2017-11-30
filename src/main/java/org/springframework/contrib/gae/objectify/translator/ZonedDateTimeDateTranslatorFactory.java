package org.springframework.contrib.gae.objectify.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * Converts JSR-310 {@link ZonedDateTime} fields to {@link Date} values for persistence in the datastore.
 */
public class ZonedDateTimeDateTranslatorFactory extends ValueTranslatorFactory<ZonedDateTime, Date> {

    /**
     * Create a new instance.
     */
    public ZonedDateTimeDateTranslatorFactory() {
        super(ZonedDateTime.class);
    }

    @Override
    protected ValueTranslator<ZonedDateTime, Date> createValueTranslator(TypeKey<ZonedDateTime> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<ZonedDateTime, Date>(Date.class) {
            @Override
            protected ZonedDateTime loadValue(Date value, LoadContext ctx, Path path) throws SkipException {
                return ZonedDateTime.ofInstant(value.toInstant(), ZoneOffset.systemDefault());
            }

            @Override
            protected Date saveValue(ZonedDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return Date.from(value.withZoneSameInstant(ZoneOffset.systemDefault()).toInstant());
            }
        };
    }
}
