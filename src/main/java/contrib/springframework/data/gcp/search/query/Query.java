package contrib.springframework.data.gcp.search.query;

import contrib.springframework.data.gcp.search.Operator;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Provides an interface to the components of a search filter.
 *
 * @param <E> Result type.
 */
public interface Query<E> {

    /**
     * @return The type of result this filter produces.
     */
    @Nonnull
    Class<E> getResultType();

    /**
     * @return The list of filter fragments.
     */
    @Nonnull
    List<Fragment> getFragments();

    /**
     * @return The specified record limit.
     */
    @Nonnull
    Optional<Integer> getLimit();


    /**
     * @return The specified skip count.
     */
    @Nonnull
    Optional<Integer> getSkip();

    /**
     * @return The specified accuracy.
     */
    @Nonnull
    Optional<Integer> getAccuracy();

    /**
     * @return The specified result sorting.
     */
    @Nonnull
    Optional<Sort> getSort();

    /**
     * @return Whether the filter will return only document ids.
     */
    boolean isIdsOnly();

    /**
     * A filter filterIn fragment.
     */
    interface Fragment {
        /**
         * @return Is this a raw filter fragment string without field or operator?
         */
        boolean isRaw();

        /**
         * @return The field this fragment applies to.
         */
        @Nonnull
        String getField();

        /**
         * @return The operator used in the filterIn.
         * @see Operator
         */
        @Nonnull
        Operator getOperator();

        /**
         * @return The value of the filterIn. If a {@link #isRaw() raw} fragment, the filter fragment itself.
         */
        @Nullable
        Object getValue();
    }
}
