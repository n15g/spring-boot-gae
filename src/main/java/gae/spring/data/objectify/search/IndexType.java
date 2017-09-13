package gae.spring.data.objectify.search;

/**
 * Controls how fields are mapped into a search index.
 */
public enum IndexType {
    AUTO,
    IDENTIFIER,
    TEXT,
    HTML,
    BIGDECIMAL,
    SMALLDECIMAL,
    DATE,
    GEOPOINT
}
