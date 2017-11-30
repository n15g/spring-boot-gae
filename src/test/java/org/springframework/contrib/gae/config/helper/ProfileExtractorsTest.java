package org.springframework.contrib.gae.config.helper;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProfileExtractorsTest {

    @Test
    public void afterLastDash() {
        String result = ProfileExtractors.AFTER_LAST_DASH.apply("lots-and-lots-of-dashes");

        assertThat(result, is("dashes"));
    }

    @Test
    public void afterLastDash_willReturnBlank_whenNoDashes() {
        String result = ProfileExtractors.AFTER_LAST_DASH.apply("nodashes");

        assertThat(result, is(""));
    }

    @Test
    public void staticValue_willReturnInpuValue() {
        assertThat(ProfileExtractors.staticValue("server").apply("anything"), is("server"));
    }

}
