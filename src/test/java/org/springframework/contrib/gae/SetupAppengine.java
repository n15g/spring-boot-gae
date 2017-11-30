package org.springframework.contrib.gae;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SetupAppengine extends ExternalResource {
    private LocalServiceTestHelper helper;

    @Override
    protected void before() throws Throwable {
        List<LocalServiceTestConfig> testConfigs = createTestConfigs();
        helper = new LocalServiceTestHelper(testConfigs.toArray(new LocalServiceTestConfig[0]));
        helper.setTimeZone(TimeZone.getDefault());
        helper.setUp();
    }

    /**
     * Override to customise the test configs required.
     *
     * @return Test configs.
     */
    protected List<LocalServiceTestConfig> createTestConfigs() {
        List<LocalServiceTestConfig> results = new ArrayList<>();
        LocalTaskQueueTestConfig queueConfig = new LocalTaskQueueTestConfig();
        queueConfig.setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml");

        results.add(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());
        results.add(queueConfig);
        results.add(new LocalMemcacheServiceTestConfig());
        results.add(new LocalSearchServiceTestConfig());
        return results;
    }

    @Override
    protected void after() {
        helper.tearDown();
    }
}
