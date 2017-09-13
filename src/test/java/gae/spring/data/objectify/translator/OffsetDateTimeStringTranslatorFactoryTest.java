package gae.spring.data.objectify.translator;

import com.googlecode.objectify.impl.translate.Translator;
import gae.spring.data.objectify.ExecuteAsTimeZone;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OffsetDateTimeStringTranslatorFactoryTest {
    private OffsetDateTimeStringTranslatorFactory factory = new OffsetDateTimeStringTranslatorFactory();
    private Translator<OffsetDateTime, String> translator = factory.createValueTranslator(null, null, null);

    @Test
    public void testSave() throws Exception {
        assertThat(
                save(OffsetDateTime.of(2017, 8, 28, 7, 9, 36, 42, ZoneOffset.UTC)),
                is("2017-08-28T07:09:36.000000042Z")
        );
    }

    @Test
    public void testSave_willConvertToUTC_whenInputHasAnotherOffset() throws Exception {
        assertThat(
                save(OffsetDateTime.of(2017, 8, 29, 3, 9, 36, 42, ZoneOffset.ofHours(10))),
                is("2017-08-28T17:09:36.000000042Z")
        );
    }

    @Test
    public void testSave_willReturnNull_whenInputIsNull() throws Exception {
        assertThat(
                save(null),
                is(nullValue())
        );
    }

    @Test
    public void testLoad() throws Exception {
        new ExecuteAsTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                .run(() -> assertThat(
                        load("2017-08-28T07:09:36.000000042Z")
                                .isEqual(OffsetDateTime.of(2017, 8, 28, 7, 9, 36, 42, ZoneOffset.UTC)),
                        is(true)
                ));
    }

    @Test
    public void testLoad_willReturnZonedDate_whenSystemDateTimeIsNotUTC() throws Exception {
        new ExecuteAsTimeZone(TimeZone.getTimeZone(ZoneOffset.ofHours(10)))
                .run(() -> assertThat(
                        load("2017-08-28T07:09:36.000000042Z")
                                .isEqual(OffsetDateTime.of(2017, 8, 28, 17, 9, 36, 42, ZoneOffset.ofHours(10))),
                        is(true)
                ));
    }

    @Test
    public void testLoad_willReturnNull_whenInputIsNull() throws Exception {
        assertThat(
                load(null),
                is(nullValue())
        );
    }

    private String save(OffsetDateTime val) {
        return translator.save(val, true, null, null);
    }

    private OffsetDateTime load(String val) {
        return translator.load(val, null, null);
    }
}