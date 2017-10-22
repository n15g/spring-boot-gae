package contrib.springframework.data.gcp.search;

/**
 * Search filtering operators.
 * Given two values, {@literal A} and {@literal B}, operators allow the interrogation of the relationship between the two.
 */
public enum Operator {
    /**
     * Does {@literal A} match {@literal B} exactly?
     */
    EQUAL("="),
    /**
     * Alias for {@link #EQUAL}.
     */
    E("="),

    /**
     * Is {@literal A} lesser in value than {@literal B}?
     * Comparison is type-dependant and a such may vary from type-to-type. i.e, {@literal 10} is less than {@literal 2}
     * in string comparison but not integer comparison.
     */
    LESS_THAN("<"),
    /**
     * Alias for {@link #LESS_THAN}.
     */
    LT("<"),

    /**
     * Is {@literal A} less than or equal in value to {@literal B}?
     * Comparison is type-dependant and a such may vary from type-to-type. i.e, {@literal 10} is less than {@literal 2}
     * in string comparison but not integer comparison.
     */
    LESS_THAN_OR_EQUAL("<="),
    /**
     * Alias for {@link #LESS_THAN_OR_EQUAL}.
     */
    LTE("<="),

    /**
     * Is {@literal A} greater in value than {@literal B}?
     * Comparison is type-dependant and a such may vary from type-to-type. i.e, {@literal 2} is greater than {@literal 10}
     * in string comparison but not integer comparison.
     */
    GREATER_THAN(">"),
    /**
     * Alias for {@link #GREATER_THAN}.
     */
    GT(">"),

    /**
     * Is {@literal A} greater than or equal in value to {@literal B}?
     * Comparison is type-dependant and a such may vary from type-to-type. i.e, {@literal 2} is greater than {@literal 10}
     * in string comparison but not integer comparison.
     */
    GREATER_THAN_OR_EQUAL(">="),
    /**
     * Alias for {@link #GREATER_THAN_OR_EQUAL}.
     */
    GTE(">="),

    /**
     * Is the value {@literal A} contained in the collection {@literal B}?
     */
    IN("in"),

    /**
     * Does any part of {@literal A} match the fragment {@literal B}?
     * Used for inexact/fuzzy searching, etc.
     */
    LIKE(":~"),

    IS(":"),
    NEAR("near");

    private String symbol;

    Operator(String keyword) {
        this.symbol = keyword;
    }

    public String getSymbol() {
        return symbol;
    }
}
