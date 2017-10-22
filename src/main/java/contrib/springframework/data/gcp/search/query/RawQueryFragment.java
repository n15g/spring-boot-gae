package contrib.springframework.data.gcp.search.query;

import contrib.springframework.data.gcp.search.Operator;

import javax.annotation.Nonnull;

/**
 * Query fragment containing just a raw query fragment.
 */
public class RawQueryFragment implements Query.Fragment {

    private String query;

    /**
     * Create a new instance.
     *
     * @param query Query fragment.
     */
    public RawQueryFragment(@Nonnull String query) {
        this.query = query;
    }

    @Override
    public boolean isRaw() {
        return true;
    }

    @Nonnull
    @Override
    public String getField() {
        throw new UnsupportedOperationException("Cannot get field on raw fragment");
    }

    @Nonnull
    @Override
    public Operator getOperator() {
        throw new UnsupportedOperationException("Cannot get operator on raw fragment");
    }

    @Nonnull
    @Override
    public String getValue() {
        return query;
    }
}
