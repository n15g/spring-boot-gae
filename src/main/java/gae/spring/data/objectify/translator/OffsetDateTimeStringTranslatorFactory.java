package gae.spring.data.objectify.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Converts JSR-310 {@link OffsetDateTime} fields to an ISO-8601 string representation for persistence in the datastore.
 * All values are converted to UTC for persistence to allow lexicographic sorting by index and to reduce zone-based idiosyncratic behaviour.
 * Supports nanosecond precision.
 */
public class OffsetDateTimeStringTranslatorFactory extends ValueTranslatorFactory<OffsetDateTime, String> {

    /**
     * Create a new instance.
     */
    public OffsetDateTimeStringTranslatorFactory() {
        super(OffsetDateTime.class);
    }

    @Override
    protected ValueTranslator<OffsetDateTime, String> createValueTranslator(TypeKey<OffsetDateTime> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<OffsetDateTime, String>(String.class) {
            @Override
            protected OffsetDateTime loadValue(String value, LoadContext ctx, Path path) throws SkipException {
                Instant instant = Instant.parse(value);
                ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(instant);
                return instant.atOffset(offset);
            }

            @Override
            protected String saveValue(OffsetDateTime value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return value.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
            }
        };
    }
}