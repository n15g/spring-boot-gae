package contrib.springframework.data.gcp.search.query;

import com.google.common.base.Function;
import org.apache.commons.text.StringEscapeUtils;

import javax.annotation.Nullable;

/**
 * Escapes quote characters in the input string and then quotes the entire value.
 * If the input string is {@code null}, {@code null} will be returned.
 */
public class QueryEscapeFunction implements Function<String, String> {

    @Override
    public String apply(@Nullable String input) {
        return quote(StringEscapeUtils.escapeJava(input));
    }

    private String quote(String s) {
        return s != null ? "\"" + s + "\"" : null;
    }
}
