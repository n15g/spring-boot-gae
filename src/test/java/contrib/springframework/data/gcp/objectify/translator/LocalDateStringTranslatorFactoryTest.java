package contrib.springframework.data.gcp.objectify.translator;

import com.googlecode.objectify.impl.translate.Translator;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class LocalDateStringTranslatorFactoryTest {
    private LocalDateStringTranslatorFactory factory = new LocalDateStringTranslatorFactory();
    private Translator<LocalDate, String> translator = factory.createValueTranslator(null, null, null);

    @Test
    public void testSave() throws Exception {
        assertThat(
                save(LocalDate.of(1776, 7, 4)),
                is("1776-07-04")
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
        assertThat(
                load("1776-07-04")
                        .isEqual(LocalDate.of(1776, 7, 4)),
                is(true)
        );
    }

    @Test
    public void testLoad_willReturnNull_whenInputIsNull() throws Exception {
        assertThat(
                load(null),
                is(nullValue())
        );
    }

    private String save(LocalDate val) {
        return translator.save(val, true, null, null);
    }

    private LocalDate load(String val) {
        return translator.load(val, null, null);
    }
}