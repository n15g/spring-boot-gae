package contrib.springframework.data.gcp.search;

/**
 * Controls how fields are mapped into a search index.
 */
public enum IndexType {
    AUTO,
    IDENTIFIER,
    TEXT,
    HTML,
    NUMBER,
    DATE,
    GEOPOINT
}
