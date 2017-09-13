package gae.spring.data.objectify.search;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field or getter as being searchable in the full text search index.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface SearchIndex {

    /**
     * Alias for {@link #name()}.
     *
     * @return {@link #name()}.
     */
    @AliasFor("name")
    String value() default "";

    /**
     * The column name to index this field under.
     *
     * @return Column name.
     */
    @AliasFor("value")
    String name() default "";

    /**
     * The {@link IndexType} the field value will be indexed as.
     *
     * @return Index type.
     */
    IndexType type() default IndexType.AUTO;
}
