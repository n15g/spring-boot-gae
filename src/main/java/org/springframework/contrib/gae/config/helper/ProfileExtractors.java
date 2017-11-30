package org.springframework.contrib.gae.config.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public interface ProfileExtractors {

    Function<String, String> AFTER_LAST_DASH = (source) -> StringUtils.substringAfterLast(source, "-");

    static Function<String, String> staticValue(String value) {
        return (source) -> value;
    }

}
