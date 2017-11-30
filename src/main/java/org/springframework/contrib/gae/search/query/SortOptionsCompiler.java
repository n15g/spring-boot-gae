package org.springframework.contrib.gae.search.query;

import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.metadata.SearchFieldMetadata;
import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.springframework.data.domain.Sort.Order;

import java.util.function.Function;

import static com.google.appengine.api.search.SortExpression.SortDirection.ASCENDING;
import static com.google.appengine.api.search.SortExpression.SortDirection.DESCENDING;
import static com.google.appengine.api.search.checkers.SearchApiLimits.MAXIMUM_DATE_VALUE;
import static com.google.appengine.api.search.checkers.SearchApiLimits.MAXIMUM_NUMBER_VALUE;
import static com.google.appengine.api.search.checkers.SearchApiLimits.MINIMUM_DATE_VALUE;
import static com.google.appengine.api.search.checkers.SearchApiLimits.MINIMUM_NUMBER_VALUE;

/**
 * Compiles a Google Search API {@link SortOptions} from a {@link Query} object.
 */
public class SortOptionsCompiler implements Function<Query<?>, SortOptions> {
    public static final String HIGH_STRING_CHAR = "\ufffd";


    final SearchMetadata searchMetadata;

    /**
     * Create a new instance.
     *
     * @param searchMetadata Search metadata.
     */
    public SortOptionsCompiler(SearchMetadata searchMetadata) {
        this.searchMetadata = searchMetadata;
    }

    @Override
    public SortOptions apply(Query<?> query) {
        SortOptions.Builder sortOptions = SortOptions.newBuilder();

        query.getSort().ifPresent(sort -> {
            for (Order order : sort) {
                SearchFieldMetadata searchFieldMetadata = searchMetadata.getField(query.getResultType(), order.getProperty());

                SortExpression.Builder expression = SortExpression.newBuilder()
                        .setExpression(searchFieldMetadata.getEncodedName())
                        .setDirection(order.getDirection().isAscending() ? ASCENDING : DESCENDING);

                applyDefaultValue(order, searchFieldMetadata.getIndexType(), expression);

                sortOptions.addSortExpression(expression.build());
            }
        });

        return sortOptions.build();
    }

    private void applyDefaultValue(Order order, IndexType indexType, SortExpression.Builder expression) {
        if (indexType == IndexType.NUMBER) {
            expression.setDefaultValueNumeric(order.isAscending() ? MAXIMUM_NUMBER_VALUE : MINIMUM_NUMBER_VALUE);
        } else if (indexType == IndexType.DATE) {
            expression.setDefaultValueDate(order.isAscending() ? MAXIMUM_DATE_VALUE : MINIMUM_DATE_VALUE);
        } else {
            expression.setDefaultValue(order.isAscending() ? HIGH_STRING_CHAR : "");
        }
    }
}
