package org.springframework.contrib.gae.search.query;

import com.google.common.collect.Lists;
import org.springframework.contrib.gae.search.Operator;
import org.springframework.contrib.gae.search.metadata.impl.MetadataUtils;
import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.List;
import java.util.function.Function;

/**
 * Compiles a filter fragment into a filter string.
 */
class QueryFragmentCompiler implements Function<Query.Fragment, String> {
    private final Class<?> entityType;
    private final SearchMetadata searchMetadata;
    private final ConversionService conversionService;
    private final QueryEscapeFunction escapeFunction = new QueryEscapeFunction();

    public QueryFragmentCompiler(Class<?> entityType, SearchMetadata searchMetadata, ConversionService conversionService) {
        this.entityType = entityType;
        this.searchMetadata = searchMetadata;
        this.conversionService = conversionService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String apply(Query.Fragment fragment) {
        if (fragment.isRaw()) {
            return String.valueOf(fragment.getValue());
        }

        String field = searchMetadata.encodeFieldName(entityType, fragment.getField());
        String operator = fragment.getOperator().getSymbol();

        if (isCollection(fragment.getValue())) {
            List<String> values = (List<String>) conversionService.convert(
                    fragment.getValue(),
                    TypeDescriptor.forObject(fragment.getValue()),
                    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class))
            );
            values = Lists.transform(values, escapeFunction);
            String value = StringUtils.join(values, " OR ");
            return String.format("%s%s(%s)", field, Operator.IS.getSymbol(), value);
        } else {
            String value = conversionService.convert(fragment.getValue(), String.class);
            value = escapeFunction.apply(value);
            return String.format("%s%s%s", field, operator, value);
        }
    }

    private boolean isCollection(Object object) {
        return MetadataUtils.isCollectionType(object.getClass());
    }
}
