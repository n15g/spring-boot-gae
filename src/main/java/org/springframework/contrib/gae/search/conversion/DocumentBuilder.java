package org.springframework.contrib.gae.search.conversion;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import org.springframework.contrib.gae.search.metadata.SearchFieldMetadata;
import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Build a search API {@link Document} from a collection of field values.
 */
public class DocumentBuilder implements BiFunction<Object, Object, Document> {

    final SearchMetadata searchMetadata;
    final ConversionService conversionService;
    final BiFunction<SearchFieldMetadata, Object, List<Field>> fieldBuilder;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service.
     */
    public DocumentBuilder(SearchMetadata searchMetadata, ConversionService conversionService) {
        this.conversionService = conversionService;
        this.searchMetadata = searchMetadata;
        this.fieldBuilder = new FieldBuilder(conversionService);
    }

    @Override
    public Document apply(Object id, Object entity) {
        String idValue = conversionService.convert(id, String.class);

        Document.Builder builder = createDocument(idValue);

        Map<String, SearchFieldMetadata> accessors = searchMetadata.getFields(entity.getClass());
        accessors.values().forEach((accessor) -> {
            List<Field> searchFields = fieldBuilder.apply(accessor, accessor.getValue(entity));

            searchFields.forEach(builder::addField);
        });

        return builder.build();
    }

    private Document.Builder createDocument(String idValue) {
        Document.Builder builder = Document.newBuilder();
        builder.setId(idValue);
        return builder;
    }
}
