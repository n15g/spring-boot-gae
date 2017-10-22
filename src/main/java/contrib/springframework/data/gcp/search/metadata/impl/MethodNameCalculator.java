package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.SearchIndex;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.apache.commons.text.WordUtils.uncapitalize;

/**
 * Returns the name for a {@link SearchIndex} annotated method.
 * <p>
 * If {@link SearchIndex#name()} is set, that will be returned. Otherwise the field name will be calculated from the method name.
 */
public class MethodNameCalculator implements Function<Method, String> {
    @Override
    public String apply(Method method) {
        SearchIndex annotation = method.getAnnotation(SearchIndex.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        }

        String name = method.getName();

        return getJavaBeanFieldName(name);
    }

    private String getJavaBeanFieldName(String result) {
        if (result.startsWith("is")) {
            result = uncapitalize(result.replaceFirst("is", ""));
        } else if (result.startsWith("get")) {
            result = uncapitalize(result.replaceFirst("get", ""));
        }
        return result;
    }
}