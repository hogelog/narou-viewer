package org.hogel.android.narouviewer.app.util;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class HtmlCacheStore {
    private final Cache<String, String> cache;

    public HtmlCacheStore() {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().maximumSize(10).softValues();
        cache = cacheBuilder.build();
    }

    public void addCache(String url, String data) {
        cache.put(url, data);
    }

    public Optional<String> findCache(String url) {
        return Optional.fromNullable(cache.getIfPresent(url));
    }
}
