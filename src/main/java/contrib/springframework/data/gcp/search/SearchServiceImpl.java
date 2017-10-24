package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import contrib.springframework.data.gcp.search.conversion.DocumentBuilder;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import contrib.springframework.data.gcp.search.misc.IndexOperation;
import contrib.springframework.data.gcp.search.query.QueryBuilder;
import org.springframework.core.convert.ConversionService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.util.concurrent.Runnables.doNothing;

/**
 * {@link SearchService} implementation.
 */
public class SearchServiceImpl implements SearchService {

    private final SearchMetadata searchMetadata;
    private final DocumentBuilder documentBuilder;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service.
     */
    public SearchServiceImpl(SearchMetadata searchMetadata, ConversionService conversionService) {
        this.searchMetadata = searchMetadata;
        documentBuilder = new DocumentBuilder(searchMetadata, conversionService);
    }

    @Nonnull
    @Override
    public <E> QueryBuilder<E> search(Class<E> entityClass) {
        throw new UnsupportedOperationException("Not implemented");//TODO: Not implemented
    }

    @Override
    public <E, I> I getId(E entity) {
        return searchMetadata.getId(entity);
    }

    @Nonnull
    @Override
    public <E, I> Runnable index(E entity, I id) {
        Index index = getIndex(entity.getClass());
        Document document = documentBuilder.apply(id, entity);

        return new IndexOperation(
                index.putAsync(document)
        );
    }

    @Nonnull
    @Override
    public <E, I> Runnable index(Map<I, E> entities) {
        if (entities.isEmpty()) {
            return doNothing();
        } else {
            Class<?> entityClass = entities.values().toArray()[0].getClass();
            Index index = getIndex(entityClass);

            List<Document> documents = entities.entrySet().stream()
                    .map(entry -> documentBuilder.apply(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            return new IndexOperation(
                    index.putAsync(documents)
            );
        }
    }

    @Nonnull
    @Override
    public <E, I> Runnable unindex(Class<E> entityClass, I id) {
        throw new UnsupportedOperationException("Not implemented");//TODO: Not implemented
    }

    @Nonnull
    @Override
    public <E, I> Runnable unindex(Class<E> entityClass, Collection<I> ids) {
        throw new UnsupportedOperationException("Not implemented");//TODO: Not implemented
    }

    @Override
    public <E> int clear(Class<E> entityClass) {
        throw new UnsupportedOperationException("Not implemented");//TODO: Not implemented
    }

    private <E> Index getIndex(Class<E> entityClass) {
        return SearchServiceFactory.getSearchService()
                .getIndex(IndexSpec.newBuilder().setName(searchMetadata.getIndexName(entityClass)));
    }
}
