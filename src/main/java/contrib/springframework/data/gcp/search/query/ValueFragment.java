package contrib.springframework.data.gcp.search.query;

import contrib.springframework.data.gcp.search.Operator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Query fragment containing just a raw value.
 */
public class ValueFragment implements Query.Fragment {

    private Object value;

    /**
     * Create a new instance.
     *
     * @param value Fragment value.
     */
    public ValueFragment(@Nullable Object value) {
        this.value = value;
    }

    @Override
    public boolean isRaw() {
        return true;
    }

    @Nonnull
    @Override
    public String getField() {
        throw new UnsupportedOperationException("Cannot get field on value fragment");
    }

    @Nonnull
    @Override
    public Operator getOperator() {
        throw new UnsupportedOperationException("Cannot get operator on value fragment");
    }

    @Nonnull
    @Override
    public Object getValue() {
        return value;
    }
}
