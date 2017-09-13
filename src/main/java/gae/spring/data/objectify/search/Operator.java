package gae.spring.data.objectify.search;

/**
 * Search filtering operators.
 */
public enum Operator {
    EQUAL("="),
    E("="),

    LESS_THAN("<"),
    LT("<"),

    LESS_THAN_OR_EQUAL("<="),
    LTE("<="),

    GREATER_THAN(">"),
    GT(">"),

    GREATER_THAN_OR_EQUAL(">="),
    GTE(">="),

    IS("is"),
    IN("in"),
    NEAR("near"),
    LIKE("like");

    private String symbol;

    Operator(String keyword) {
        this.symbol = keyword;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
