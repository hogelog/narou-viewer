package org.hogel.android.narouviewer.app.util;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlCacheStore implements RemovalListener<Object, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(HtmlCacheStore.class);

    private final Cache<String, String> cache;

    public HtmlCacheStore() {
        CacheBuilder<Object, Object> cacheBuilder =
                CacheBuilder
                        .newBuilder()
                        .maximumSize(10)
                        .softValues()
                        .removalListener(this);
        cache = cacheBuilder.build();
    }

    @Override
    public void onRemoval(RemovalNotification<Object, Object> notification) {
        LOG.info("removed: {}", notification.getKey());
    }

    public void addCache(String url, String data) {
        LOG.info("cached: {}", url);
        cache.put(url, data);
    }

    public Optional<String> findCache(String url) {
        return Optional.fromNullable(cache.getIfPresent(url));
    }

    public boolean hasCache(String url) {
        return cache.getIfPresent(url) != null;
    }
}
