package org.springframework.contrib.gae.objectify.translator;

import com.googlecode.objectify.impl.translate.Translator;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LocalDateDateTranslatorFactoryTest {

    private Translator<LocalDate, Date> translator;

    @Before
    public void before() {
        translator = new LocalDateDateTranslatorFactory().createValueTranslator(null, null, null);
    }

    @Test
    public void save_willConvertToDate() {
        LocalDate localDate = LocalDate.parse("2017-12-01");

        Date result = save(localDate);

        assertThat(result, equalTo(date("2017-12-01T00:00:00Z")));
    }

    @Test
    public void load_willConvertToLocalDate() {
        Date date = date("2017-12-01T00:00:00Z");

        LocalDate result = load(date);

        assertThat(result, equalTo(LocalDate.parse("2017-12-01")));
    }

    @Test
    public void save_load_willRetainSameDate() {
        LocalDate localDate = LocalDate.parse("2017-12-01");

        Date saved = save(localDate);
        LocalDate result = load(saved);


        assertThat(result, equalTo(localDate));
        assertThat(save(result), equalTo(saved));
    }

    private Date save(LocalDate val) {
        return translator.save(val, true, null, null);
    }

    private LocalDate load(Date val) {
        return translator.load(val, null, null);
    }

    private Date date(String val) {
        return Date.from(Instant.parse(val));
    }
}