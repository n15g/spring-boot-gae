package org.springframework.contrib.gae.objectify.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;


/**
 * Converts JSR-310 {@link OffsetDateTime} fields to {@link Date} values for persistence in the datastore.
 */
public class OffsetDateTimeDateTranslatorFactory extends ValueTranslatorFactory<OffsetDateTime, Date> {

    /**
     * Create a new instance.
     */
    public OffsetDateTimeDateTranslatorFactory() {
        super(OffsetDateTime.class);
    }

    @Override
    protected ValueTranslator<OffsetDateTime, Date> createValueTranslator(TypeKey<OffsetDateTime> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<OffsetDateTime, Date>(Date.class) {
            @Override
            protected OffsetDateTime loadValue(Date value, LoadContext ctx, Path path) throws SkipException {
                return OffsetDateTime.ofInstant(value.toInstant(), ZoneOffset.systemDefault());
            }

            @Override
            protected Date saveValue(OffsetDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return Date.from(value.atZoneSameInstant(ZoneOffset.systemDefault()).toInstant());
            }
        };
    }
}
