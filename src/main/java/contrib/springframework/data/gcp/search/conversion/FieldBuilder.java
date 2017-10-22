package contrib.springframework.data.gcp.search.conversion;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import contrib.springframework.data.gcp.search.IndexType;
import contrib.springframework.data.gcp.search.metadata.Accessor;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Build an array of search service {@link Field}s from a field {@link Accessor}.
 * If the value of the field the accessor
 */
public class FieldBuilder implements BiFunction<Accessor, Object, List<Field>> {

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
    public List<Field> apply(Accessor accessor, Object entity) {
        Collection<?> values = values(accessor.getValue(entity));
        BiConsumer<Field.Builder, Object> mutator = getMutator(accessor.getIndexType());

        return values.stream()
                .map(value -> {
                    Field.Builder field = Field.newBuilder();
                    field.setName(accessor.getEncodedName());
                    mutator.accept(field, value);
                    return field.build();
                }).collect(Collectors.toList());
    }

    private Collection values(Object value) {
        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        } else {
            if (value.getClass().isAssignableFrom(Collection.class)) {
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
                return this::setNumber;
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
