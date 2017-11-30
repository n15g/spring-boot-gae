package org.springframework.contrib.gae;

import org.junit.rules.ExternalResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Resets specified properties after each test
 */
public class SystemPropertyReset extends ExternalResource {

    private final Map<String, String> originalValues;
    private final Set<String> originalNulls;

    public SystemPropertyReset(String... properties) {
        this.originalValues = new HashMap<>();
        this.originalNulls = new HashSet<>();
        for (String property : properties) {
            String propValue = System.getProperty(property);
            if (propValue == null) {
                originalNulls.add(property);
            } else {
                originalValues.put(property, propValue);
            }
        }
    }

    @Override
    protected void after() {
        originalValues.forEach(System::setProperty);
        originalNulls.forEach(System::clearProperty);
    }
}
