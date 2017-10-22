package contrib.springframework.data.gcp.search.metadata;


import java.util.function.Function;

/**
 * Defines the mechanism for converting a searchable entity class into an index name.
 */
public interface IndexNamingStrategy extends Function<Class<?>, String> {
}
