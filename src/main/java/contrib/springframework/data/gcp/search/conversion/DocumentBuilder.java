package contrib.springframework.data.gcp.search.conversion;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import contrib.springframework.data.gcp.search.metadata.Accessor;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Build a search API {@link Document} from a collection of field values.
 *
 * @param <I> Document id type.
 */
public class DocumentBuilder<I> implements BiFunction<I, Map<Accessor, Object>, Document> {

    final ConversionService conversionService;
    final BiFunction<Accessor, Object, List<Field>> fieldBuilder;

    /**
     * Create a new instance.
     *
     * @param conversionService Conversion service.
     */
    public DocumentBuilder(ConversionService conversionService) {
        this.conversionService = conversionService;
        this.fieldBuilder = new FieldBuilder(conversionService);
    }

    @Override
    public Document apply(I id, Map<Accessor, Object> fieldValues) {
        String idValue = conversionService.convert(id, String.class);

        Document.Builder builder = createDocument(idValue);

        fieldValues.forEach((accessor, value) -> {
            List<Field> fields = fieldBuilder.apply(accessor, value);

            fields.forEach(builder::addField);
        });

        return builder.build();
    }

    private Document.Builder createDocument(String idValue) {
        Document.Builder builder = Document.newBuilder();
        builder.setId(idValue);
        return builder;
    }
}
