package org.springframework.contrib.gae.search.query;

import org.springframework.contrib.gae.search.Operator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Query fragment with an {@link Operator} and value to test against.
 */
public class PredicateQueryFragment implements Query.Fragment {
    private String field;
    private Operator operator;
    private Object value;

    /**
     * Create a new instance.
     *
     * @param field    Field name.
     * @param operator Predicate operator.
     * @param value    Predicate value.
     */
    public PredicateQueryFragment(String field, Operator operator, @Nullable Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean isRaw() {
        return false;
    }

    @Nonnull
    @Override
    public String getField() {
        return field;
    }

    @Nonnull
    @Override
    public Operator getOperator() {
        return operator;
    }

    @Nullable
    @Override
    public Object getValue() {
        return value;
    }
}
