package contrib.springframework.data.gcp.objectify.config;

import contrib.springframework.data.gcp.objectify.ObjectifyProxy;

/**
 * Provides a static method to access the {@link ObjectifyProxy}.
 * This is an alternative to directly accessing {@link com.googlecode.objectify.ObjectifyService} where it's impractical or impossible
 * to have the proxy bean injected.
 * <p>
 * This bean must be registered and initialized before attempting to access the proxy, or an {@link IllegalStateException} will be thrown.
 */
public class StaticObjectifyProxy {
    private static ObjectifyProxy proxy;

    /**
     * Set the proxy reference.
     *
     * @param proxy Objectify proxy.
     */
    public StaticObjectifyProxy(ObjectifyProxy proxy) {
        StaticObjectifyProxy.proxy = proxy;
    }

    /**
     * @return Objectify proxy.
     * @throws IllegalStateException If this bean has not been initialized.
     */
    public static ObjectifyProxy get() {
        if (proxy == null) {
            throw new IllegalStateException("StaticObjectifyProxy bean has not been initialized");
        }
        return proxy;
    }
}
