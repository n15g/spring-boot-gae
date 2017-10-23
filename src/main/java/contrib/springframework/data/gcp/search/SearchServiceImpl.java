package contrib.springframework.data.gcp.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.SearchServiceFactory;
import contrib.springframework.data.gcp.search.conversion.DocumentBuilder;
import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import contrib.springframework.data.gcp.search.query.QueryBuilder;
import org.springframework.core.convert.ConversionService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * {@link SearchService} implementation.
 */
public class SearchServiceImpl implements SearchService {

    private final SearchMetadata searchMetadata;
    private final ConversionService conversionService;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service.
     */
    public SearchServiceImpl(SearchMetadata searchMetadata,
                             ConversionService conversionService) {
        this.searchMetadata = searchMetadata;
        this.conversionService = conversionService;
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
        Document document = new DocumentBuilder<I>(conversionService).apply(id, searchMetadata.getFieldValues(entity));
        Index index = getIndex(entity);

        return resolve(
                index.putAsync(document)
        );
    }

    @Nonnull
    @Override
    public <E, I> Runnable index(Map<I, E> entities) {
        throw new UnsupportedOperationException("Not implemented"); //TODO: Not implemented
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

    private <E> Index getIndex(E entity) {
        return SearchServiceFactory.getSearchService()
                .getIndex(IndexSpec.newBuilder().setName(searchMetadata.getIndexName(entity.getClass())));
    }

    private Runnable resolve(Future<PutResponse> putAsync) {
        return () -> {
            try {
                putAsync.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IndexException(e);
            }
        };
    }
}
