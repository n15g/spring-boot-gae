package contrib.springframework.data.gcp.search;

import contrib.springframework.data.gcp.search.metadata.SearchMetadata;
import contrib.springframework.data.gcp.search.query.QueryBuilder;
import org.springframework.core.convert.ConversionService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

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
        throw new UnsupportedOperationException("Not implemented"); //TODO: Not implemented
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
}
