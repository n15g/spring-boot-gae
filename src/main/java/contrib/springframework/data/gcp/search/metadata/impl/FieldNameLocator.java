package contrib.springframework.data.gcp.search.metadata.impl;

import contrib.springframework.data.gcp.search.SearchIndex;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Returns the name for a {@link SearchIndex} annotated method.
 * <p>
 * If {@link SearchIndex#name()} is set, that will be returned. Otherwise the actual field name will be used.
 */
public class FieldNameLocator implements Function<Field, String> {
    @Override
    public String apply(Field field) {
        SearchIndex annotation = field.getAnnotation(SearchIndex.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        }

        return field.getName();
    }
}