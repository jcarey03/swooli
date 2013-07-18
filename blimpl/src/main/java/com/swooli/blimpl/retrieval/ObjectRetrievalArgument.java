package com.swooli.blimpl.retrieval;

import com.swooli.blimpl.cache.redis.CacheKey;

public interface ObjectRetrievalArgument<T> {

    Long getId();

    String getKey();

    CacheKey getCacheKey();

    Class<T> getTargetClass();
}
