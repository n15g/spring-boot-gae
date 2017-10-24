package contrib.springframework.data.gcp.search.misc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Runnable that wraps a {@link Future} operation that does not return a value.
 * Calling {@link Runnable#run()} should just synchronously complete the {@link Future} operation.
 */
public class IndexOperation implements Runnable {

    private final Future<?> future;

    /**
     * Create a new instance.
     *
     * @param future The {@link Future} to wrap.
     */
    public IndexOperation(Future<?> future) {
        this.future = future;
    }

    @Override
    public void run() {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IndexException(e);
        }
    }
}
