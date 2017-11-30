package org.springframework.contrib.gae.config.helper;

import com.google.appengine.api.utils.SystemProperty;
import org.springframework.contrib.gae.SystemPropertyReset;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileResolverTest {

    @Mock
    private Function<String, String> extractor;

    @Rule
    public SystemPropertyReset systemPropertyReset = new SystemPropertyReset(SystemProperty.applicationId.key(), SystemProperty.environment.key());

    private ProfileResolver profileResolver;

    @Before
    public void before() {
        profileResolver = new ProfileResolver();
    }

    @Test
    public void getProfiles_willReturnLocal_whenEnvironmentNotProduction() {
        SystemProperty.environment.set(SystemProperty.Environment.Value.Development);

        List<String> profiles = profileResolver.getProfiles();

        assertThat(profiles, contains("local"));
    }

    @Test
    public void getProfiles_willReturnApplicationIdProperty_whenEnvironmentProduction() {
        SystemProperty.environment.set(SystemProperty.Environment.Value.Production);
        SystemProperty.applicationId.set("my-application-dev");


        List<String> profiles = profileResolver.getProfiles();

        assertThat(profiles, contains("my-application-dev"));
    }

    @Test
    public void getProfiles_willReturnEmptyList_whenEnvironmentProduction_andNoApplicationId() {
        SystemProperty.environment.set(SystemProperty.Environment.Value.Production);

        List<String> profiles = profileResolver.getProfiles();

        assertThat(profiles, hasSize(0));
    }

    @Test
    public void getProfiles_willAddValueFromExtractor_whenEnvironmentProduction_andApplicationIdSet_andExtractorFunctionSupplied() {
        SystemProperty.environment.set(SystemProperty.Environment.Value.Production);
        SystemProperty.applicationId.set("my-application-dev");
        when(extractor.apply("my-application-dev")).thenReturn("dev");

        List<String> profiles = profileResolver.setAdditionalProfileExtractor(extractor)
            .getProfiles();

        assertThat(profiles, contains("dev", "my-application-dev"));
    }

    @Test
    public void getProfiles_willNotAddBlank_whenEnvironmentProduction_andApplicationIdSet_andExtractorFunctionSupplied() {
        SystemProperty.environment.set(SystemProperty.Environment.Value.Production);
        SystemProperty.applicationId.set("my-application-dev");
        when(extractor.apply("my-application-dev")).thenReturn("  ");

        List<String> profiles = profileResolver.setAdditionalProfileExtractor(extractor)
            .getProfiles();

        assertThat(profiles, contains("my-application-dev"));
    }

}
