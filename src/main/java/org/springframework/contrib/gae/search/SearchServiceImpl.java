package org.springframework.contrib.gae.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.common.util.concurrent.Runnables;
import org.springframework.contrib.gae.search.conversion.DocumentBuilder;
import org.springframework.contrib.gae.search.query.Query;
import org.springframework.contrib.gae.search.query.QueryBuilder;
import org.springframework.contrib.gae.search.query.QueryCompiler;
import org.springframework.contrib.gae.search.query.Result;
import org.springframework.contrib.gae.search.metadata.SearchMetadata;
import org.springframework.contrib.gae.search.misc.IndexOperation;
import org.springframework.contrib.gae.search.query.QueryImpl;
import org.springframework.contrib.gae.search.query.ResultImpl;
import org.springframework.core.convert.ConversionService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
    private final ConversionService conversionService;
    private QueryCompiler queryCompiler;

    /**
     * Create a new instance.
     *
     * @param searchMetadata    Search metadata.
     * @param conversionService Conversion service.
     */
    public SearchServiceImpl(SearchMetadata searchMetadata, ConversionService conversionService) {
        this.searchMetadata = searchMetadata;
        this.conversionService = conversionService;
        documentBuilder = new DocumentBuilder(searchMetadata, conversionService);
        queryCompiler = new QueryCompiler(searchMetadata, conversionService);
    }

    @Nonnull
    @Override
    public <E> QueryBuilder<E> createQuery(Class<E> entityClass) {
        return new QueryImpl<>(entityClass);
    }

    @Override
    public Result<ScoredDocument> execute(Query<?> query) {
        Results<ScoredDocument> result = getIndex(query.getResultType())
                .search(queryCompiler.apply(query));

        return new ResultImpl<>(result, x -> new ArrayList<>(x.getResults()));
    }

    @Override
    public <E> String getId(E entity) {
        return searchMetadata.getId(entity);
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(E entity, String id) {
        if (!searchMetadata.hasIndexedFields(entity.getClass())) {
            return Runnables.doNothing();
        }

        Index index = getIndex(entity.getClass());
        Document document = documentBuilder.apply(id, entity);

        return new IndexOperation(
                index.putAsync(document)
        );
    }

    @Nonnull
    @Override
    public <E> Runnable indexAsync(Map<String, E> entities) {
        if (entities.isEmpty()) {
            return doNothing();
        }

        Class<?> entityClass = entities.values().toArray()[0].getClass();
        if (!searchMetadata.hasIndexedFields(entityClass)) {
            return doNothing();
        }

        List<Document> documents = entities.entrySet().stream()
                .map(entry -> documentBuilder.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        Index index = getIndex(entityClass);
        return new IndexOperation(
                index.putAsync(documents)
        );
    }

    @Override
    public <E> void unindex(Class<E> entityClass, String id) {
        String idString = conversionService.convert(id, String.class);
        getIndex(entityClass).delete(idString);
    }

    @Override
    public <E> void unindex(Class<E> entityClass, Collection<String> ids) {
        getIndex(entityClass).delete(ids);
    }

    @Override
    public <E> int clear(Class<E> entityClass) {
        Index index = getIndex(entityClass);

        GetRequest request = GetRequest.newBuilder()
                .setReturningIdsOnly(true)
                .setLimit(200) //Delete only allows 200 records at a time.
                .build();

        int count = 0;
        for (List<Document> documents = getDocumentIds(index, request); !documents.isEmpty(); documents = getDocumentIds(index, request)) {
            count += documents.size();
            unindex(entityClass, documents.stream().map(Document::getId));
        }
        return count;
    }

    private List<Document> getDocumentIds(Index index, GetRequest request) {
        return index.getRange(request).getResults();
    }

    private <E> Index getIndex(Class<E> entityClass) {
        return SearchServiceFactory.getSearchService()
                .getIndex(IndexSpec.newBuilder().setName(searchMetadata.getIndexName(entityClass)));
    }
}
