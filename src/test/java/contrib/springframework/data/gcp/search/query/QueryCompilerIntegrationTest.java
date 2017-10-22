package contrib.springframework.data.gcp.search.query;

import contrib.springframework.data.gcp.objectify.ObjectifyTest;
import contrib.springframework.data.gcp.search.Operator;
import contrib.springframework.data.gcp.search.TestSearchEntity;
import contrib.springframework.data.gcp.search.conversion.DefaultSearchConversionService;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class QueryCompilerIntegrationTest extends ObjectifyTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Autowired
    private SearchMetadata searchMetadata;

    private ConversionService conversionService = new DefaultSearchConversionService();

    private QueryCompiler<TestSearchEntity> compiler;

    @Before
    public void setUp() throws Exception {
        compiler = new QueryCompiler<>(TestSearchEntity.class, searchMetadata, conversionService);
    }

    @Test
    public void apply() {
        Query<TestSearchEntity> query = query()
                .filter("stringField", Operator.EQUAL, "stringValue")
                .filter("longField", Operator.GREATER_THAN, 3)
                .filter("id", "id1", "id2")
                .filter("stringArrayField", Operator.LIKE, "likeValue")
                .order("stringField", DESC)
                .skip(1)
                .accuracy(2)
                .limit(3)
                .build();

        assertThat(compiler.apply(query))
                .isEqualTo("stringField=\"stringValue\" longField>\"3\" id:(\"id1\" OR \"id2\") stringArrayField:~\"likeValue\"");
    }

    private QueryImpl<TestSearchEntity> query() {
        return new QueryImpl<>(TestSearchEntity.class, null);
    }
}
