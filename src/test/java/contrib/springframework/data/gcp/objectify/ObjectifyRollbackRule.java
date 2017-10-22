package contrib.springframework.data.gcp.objectify;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.rules.ExternalResource;

/**
 * Create an objectify unit of work for this test, then roll it back once the test is complete.
 * Prevents tests from polluting the datastore with throwaway data.
 */
public class ObjectifyRollbackRule extends ExternalResource {

    private Closeable closeable;

    @Override
    protected void before() throws Throwable {
        super.before();
        closeable = ObjectifyService.begin();
    }

    @Override
    protected void after() {
        super.after();
        closeable.close();
    }
}
