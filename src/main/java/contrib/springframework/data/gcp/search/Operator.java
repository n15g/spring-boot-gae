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
     * To search for common variations of a word, like plural forms and verb endings, use the ~ stem operator (the tilde character).
     * This is a prefix operator which must precede a value with no intervening space. The value ~cat will match "cat" or "cats,"
     * and likewise ~dog matches "dog" or "dogs." The stemming algorithm is not fool-proof. The value ~care will match "care" and
     * "caring," but not "cares" or "cared." Stemming is only used when searching text and HTML fields.
     * https://cloud.google.com/appengine/docs/standard/java/search/query_strings
     */
    STEM("=~"),

    IS(":");

    private String symbol;

    Operator(String keyword) {
        this.symbol = keyword;
    }

    public String getSymbol() {
        return symbol;
    }
}
