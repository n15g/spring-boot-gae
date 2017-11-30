package org.springframework.contrib.gae.search.metadata.impl;

import java.util.function.Function;

/**
 * Encode a field name, ensuring it is suitable for use in a search index.
 */
public class FieldNameEncoder implements Function<String, String> {
    @Override
    public String apply(String name) {
        return name
                .replaceAll("[^\\w]", "_")
                .replaceAll("[^a-zA-Z]*([a-zA-Z]+)([\\w]*)", "$1$2");
    }
}
