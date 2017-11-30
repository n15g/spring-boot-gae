package org.springframework.contrib.gae.search.metadata.impl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldNameEncoderTest {
    private FieldNameEncoder encoder = new FieldNameEncoder();

    @Test
    public void apply_willNotEffectAGoodFieldName() throws Exception {
        assertThat(encoder.apply("fieldName")).isEqualTo("fieldName");
    }

    @Test
    public void apply_willNotEffectInternalUnderscores() throws Exception {
        assertThat(encoder.apply("field_name")).isEqualTo("field_name");
    }

    @Test
    public void apply_willNotEffectBadCase() throws Exception {
        assertThat(encoder.apply("FieldName")).isEqualTo("FieldName");
    }

    @Test
    public void apply_willStripLeadingUnderscore() throws Exception {
        assertThat(encoder.apply("_field")).isEqualTo("field");
    }

    @Test
    public void apply_willReplaceHyphenWithUnderscore() throws Exception {
        assertThat(encoder.apply("some-field")).isEqualTo("some_field");
    }

    @Test
    public void apply_willReplaceWhiteSpace() throws Exception {
        assertThat(encoder.apply("field name")).isEqualTo("field_name");
    }

    @Test
    public void apply_willReplaceNonAlphanumericCharactersWithUnderscore() throws Exception {
        assertThat(encoder.apply("fi#|d n@m*")).isEqualTo("fi__d_n_m_");
    }

}