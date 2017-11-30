package org.springframework.contrib.gae.search.query;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * Result from a search execution.
 *
 * @param <T> Result type.
 */
public interface Result<T> extends Iterable<T> {
    /**
     * @return List of results.
     */
    List<T> getList();

    /**
     * @return Metadata and raw search results.
     */
    Results<ScoredDocument> getMetadata();

    /**
     * @return Total number of records found.
     */
    default long getTotal() {
        return getMetadata().getNumberFound();
    }

    /**
     * @return Number of records returned.
     */
    default int getCount() {
        return getMetadata().getNumberReturned();
    }

    @Nonnull
    @Override
    default Iterator<T> iterator() {
        return getList().iterator();
    }
}
