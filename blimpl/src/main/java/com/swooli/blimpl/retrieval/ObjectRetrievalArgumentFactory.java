package com.swooli.blimpl.retrieval;

import com.swooli.blimpl.cache.redis.CacheKey;

public class ObjectRetrievalArgumentFactory {

    public static <T> ObjectRetrievalArgument<T> create(final long id, final CacheKey cacheKey, final Class<T> clazz) {

        return new ObjectRetrievalArgument<T>() {

            @Override
            public CacheKey getCacheKey() {
                return cacheKey;
            }

            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getKey() {
                return cacheKey.getKey(getId());
            }

            @Override
            public Class<T> getTargetClass() {
                return clazz;
            }

        };
    }
}