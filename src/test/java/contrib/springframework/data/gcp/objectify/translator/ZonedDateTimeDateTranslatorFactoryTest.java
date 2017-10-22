package contrib.springframework.data.gcp.objectify.translator;

import com.googlecode.objectify.impl.translate.Translator;
import contrib.springframework.data.gcp.objectify.ExecuteAsTimeZone;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ZonedDateTimeDateTranslatorFactoryTest {

    private ZonedDateTimeDateTranslatorFactory factory = new ZonedDateTimeDateTranslatorFactory();
    private Translator<ZonedDateTime, Date> translator = factory.createValueTranslator(null, null, null);

    @Test
    public void testSave() throws Exception {
        assertThat(
                save(ZonedDateTime.of(2017, 8, 28, 7, 9, 36, 42, ZoneOffset.UTC)),
                is(date("2017-08-28T07:09:36Z"))
        );
    }

    @Test
    public void testSave_willConvertToUTC_whenInputHasAnotherZoned() throws Exception {
        assertThat(
                save(ZonedDateTime.of(2017, 8, 29, 3, 9, 36, 42, ZoneOffset.ofHours(10))),
                is(date("2017-08-28T17:09:36Z"))
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
                        load(date("2017-08-28T07:09:36Z"))
                                .isEqual(ZonedDateTime.of(2017, 8, 28, 7, 9, 36, 0, ZoneOffset.UTC)),
                        is(true)
                ));
    }

    @Test
    public void testLoad_willReturnZonedDate_whenSystemDateTimeIsNotUTC() throws Exception {
        new ExecuteAsTimeZone(TimeZone.getTimeZone(ZoneOffset.ofHours(10)))
                .run(() -> assertThat(
                        load(date("2017-08-28T07:09:36Z"))
                                .isEqual(ZonedDateTime.of(2017, 8, 28, 17, 9, 36, 0, ZoneOffset.ofHours(10))),
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

    private Date save(ZonedDateTime val) {
        return translator.save(val, true, null, null);
    }

    private ZonedDateTime load(Date val) {
        return translator.load(val, null, null);
    }

    private Date date(String val) {
        return Date.from(Instant.parse(val));
    }
}