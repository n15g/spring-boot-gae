package contrib.springframework.data.gcp.search.query;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryEscapeFunctionTest {
    private QueryEscapeFunction function = new QueryEscapeFunction();

    @Test
    public void apply_willNotModify_whenStringDoesNotContainQuoteCharacters() {
        assertThat(function.apply("Some string")).isEqualTo("\"Some string\"");
    }

    @Test
    public void apply_willQuote_whenStringDoesContainsQuoteCharacters() {
        assertThat(function.apply("Some string \"with\" quotes.")).isEqualTo("\"Some string \\\"with\\\" quotes.\"");
    }

    @Test
    public void apply_willReturnNull_whenInputIsNull() {
        assertThat(function.apply(null)).isNull();
    }
}