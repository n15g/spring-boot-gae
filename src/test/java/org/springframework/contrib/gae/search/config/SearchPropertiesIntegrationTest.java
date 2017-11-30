package org.springframework.contrib.gae.search.config;

import org.springframework.contrib.gae.search.metadata.IndexNamingStrategy;
import org.springframework.contrib.gae.search.metadata.impl.FQIndexNamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("properties-test")
@SpringBootTest(classes = SearchTestConfiguration.class)
@RunWith(SpringRunner.class)
public class SearchPropertiesIntegrationTest {

    @Autowired
    private IndexNamingStrategy indexNamingStrategy;
    
    @Test
    public void indexNamingStrategy_willBeDefault_whenNotSpecifiedInPropertiesFile() {
        assertThat(indexNamingStrategy).isInstanceOf(FQIndexNamingStrategy.class);
    }
}