package gae.spring.data.objectify;

import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * Execute a block of code with a certain default time zone set.
 */
public class ExecuteAsTimeZone {

    private final TimeZone newTimeZone;

    public ExecuteAsTimeZone(TimeZone newTimeZone) {
        this.newTimeZone = newTimeZone;
    }

    public void run(Runnable runnable) {
        TimeZone previousTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(this.newTimeZone);
        try {
            runnable.run();
        } finally {
            TimeZone.setDefault(previousTimeZone);
        }
    }

    public <T> T run(Supplier<T> supplier) {
        TimeZone previousTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(this.newTimeZone);
        try {
            return supplier.get();
        } finally {
            TimeZone.setDefault(previousTimeZone);
        }
    }
}
