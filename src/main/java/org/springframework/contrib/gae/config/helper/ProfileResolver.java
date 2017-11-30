package org.springframework.contrib.gae.config.helper;

import com.google.appengine.api.utils.SystemProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ProfileResolver {
    private List<Function<String, String>> extractorFunctions = new ArrayList<>();

    /**
     * Extract additional profile name from the {@link SystemProperty#applicationId} when the
     * environment is not local development.
     *
     * @param extractorFunction Function used to retrieve the additional profile value.
     *
     * @return this
     */
    public ProfileResolver setAdditionalProfileExtractor(Function<String, String> extractorFunction) {
        this.extractorFunctions.add(extractorFunction);
        return this;
    }

    public List<String> getProfiles() {
        List<String> profiles = new ArrayList<>();

        if (isProduction()) {
            String applicationId = SystemProperty.applicationId.get();
            if (StringUtils.isNotBlank(applicationId)) {
                for (Function<String, String> extractorFunction : extractorFunctions) {
                    String extracted = extractorFunction.apply(applicationId);
                    if (StringUtils.isNotBlank(extracted)) {
                        profiles.add(extracted);
                    }
                }
                // We want the original applicationId to be the winning profile as it should be more specific
                profiles.add(applicationId);
            }
        } else {
            profiles.add("local");
        }
        return profiles;
    }

    private boolean isProduction() {
        return Objects.equals(SystemProperty.Environment.Value.Production.name(), SystemProperty.Environment.environment.get());
    }
}
