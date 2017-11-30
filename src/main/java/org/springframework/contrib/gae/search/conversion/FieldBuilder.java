package org.springframework.contrib.gae.search.conversion;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.metadata.SearchFieldMetadata;
import org.springframework.contrib.gae.search.metadata.impl.MetadataUtils;
import org.springframework.contrib.gae.search.misc.IndexException;
import org.springframework.core.convert.ConversionService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Build an array of search service {@link Field}s from a field {@link SearchFieldMetadata}.
 */
public class FieldBuilder implements BiFunction<SearchFieldMetadata, Object, List<Field>> {

    private final ConversionService conversionService;

    /**
     * Create a new instance.
     *
     * @param conversionService Conversion service to use to convert input values to the appropriate Search Service type.
     */
    public FieldBuilder(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public List<Field> apply(SearchFieldMetadata searchFieldMetadata, @Nullable Object fieldValue) {
        assertSupportedMultiplicity(searchFieldMetadata, fieldValue);

        Collection<?> values = toValueList(fieldValue);
        BiConsumer<Field.Builder, Object> mutator = getMutator(searchFieldMetadata.getIndexType());

        return values.stream()
                .map(value -> {
                    Field.Builder field = Field.newBuilder();
                    field.setName(searchFieldMetadata.getEncodedName());
                    mutator.accept(field, value);
                    return field.build();
                }).collect(Collectors.toList());
    }

    /**
     * Search only supports multiple values if the index type is not {@link IndexType#NUMBER} or {@link IndexType#DATE}.
     * See https://cloud.google.com/appengine/docs/standard/java/search/ - Multi-valued fields.
     *
     * @param searchFieldMetadata   Field searchFieldMetadata.
     * @param fieldValue The value of the field.
     */
    private void assertSupportedMultiplicity(SearchFieldMetadata searchFieldMetadata, @Nullable Object fieldValue) {
        if (fieldValue == null) {
            return;
        }

        IndexType indexType = searchFieldMetadata.getIndexType();

        if (MetadataUtils.isCollectionType(fieldValue.getClass())
                && (indexType == IndexType.NUMBER || indexType == IndexType.DATE)) {
            throw new IndexException("Search does not support multiplicity on NUMBER or DATE index types. Offending member: " + searchFieldMetadata.getMember());
        }
    }

    @Nonnull
    private Collection toValueList(@Nullable Object value) {
        if (value == null) {
            return Collections.singletonList(null);
        }
        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        } else {
            if (Collection.class.isAssignableFrom(value.getClass())) {
                return (Collection) value;
            }
        }

        return Collections.singletonList(value);
    }

    private BiConsumer<Field.Builder, Object> getMutator(IndexType indexType) {
        switch (indexType) {
            case IDENTIFIER:
                return this::setAtom;
            case NUMBER:
                return this::setNumber;
            case HTML:
                return this::setHtml;
            case DATE:
                return this::setDate;
            case GEOPOINT:
                return this::setGeopoint;
            default:
                return this::setText;
        }
    }

    private void setAtom(Field.Builder field, Object value) {
        String normalized = conversionService.convert(value, String.class);
        field.setAtom(normalized);
    }

    private void setText(Field.Builder field, Object value) {
        String normalized = conversionService.convert(value, String.class);
        field.setText(normalized);
    }

    private void setHtml(Field.Builder field, Object value) {
        String normalized = conversionService.convert(value, String.class);
        field.setHTML(normalized);
    }

    private void setNumber(Field.Builder field, Object value) {
        Double normalized = conversionService.convert(value, Double.class);
        field.setNumber(normalized);
    }

    private void setDate(Field.Builder field, Object value) {
        Date normalized = conversionService.convert(value, Date.class);
        field.setDate(normalized);
    }

    private void setGeopoint(Field.Builder field, Object value) {
        GeoPoint normalized = conversionService.convert(value, GeoPoint.class);
        field.setGeoPoint(normalized);
    }
}
