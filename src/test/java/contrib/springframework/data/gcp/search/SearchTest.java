package contrib.springframework.data.gcp.search;

import contrib.springframework.data.gcp.SetupAppengine;
import contrib.springframework.data.gcp.objectify.ObjectifyRollbackRule;
import contrib.springframework.data.gcp.search.config.SearchTestConfiguration;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchTestConfiguration.class})
public abstract class SearchTest {
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();

    @Rule
    public ObjectifyRollbackRule objectifyRollbackRule = new ObjectifyRollbackRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}
