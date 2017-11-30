package org.springframework.contrib.gae.search.metadata.impl;

import org.springframework.contrib.gae.search.SearchIndex;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldNameLocatorTest {
    private Function<Field, String> analyzer = new FieldNameLocator();

    @Test
    public void apply_willHandleStraightforwardField() throws Exception {
        assertThat(analyzer.apply(Fields.class.getDeclaredField("straightForwardField")))
                .isEqualTo("straightForwardField");
    }

    @Test
    public void apply_willHandleNamedField() throws Exception {
        assertThat(analyzer.apply(Fields.class.getDeclaredField("namedField"))
        ).isEqualTo("anotherName");
    }

    @Test
    public void apply_willHandleUnannotatedField() throws Exception {
        assertThat(analyzer.apply(Fields.class.getDeclaredField("unannotatedField")))
                .isEqualTo("unannotatedField");
    }

    @Test
    public void apply_willHandleFieldWithUnderscore() throws Exception {
        assertThat(analyzer.apply(Fields.class.getDeclaredField("field_withAnUnderscore")))
                .isEqualTo("field_withAnUnderscore");
    }

    private class Fields {
        @SearchIndex
        String straightForwardField;

        @SearchIndex("anotherName")
        String namedField;

        String unannotatedField;

        @SearchIndex
        String field_withAnUnderscore;
    }
}