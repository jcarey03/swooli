package com.swooli.blimpl.cache.redis;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CacheKey {

//    CATEGORY_COLLECTION_PREVIEW("category:{}:collection:preview", "redis.key.category.collection.preview.ttl", new int[] {1}, -1),
//    COLLECTION_PREVIEW("collection:preview:{}", "redis.key.collection.preview.ttl", new int[] {2}, 0),
//    VIDEO("video:{}", "redis.key.video.ttl", new int[] {1}, 0),
//    VIDEO_ROOT("video:root:{}", "redis.key.video.root.ttl", new int[] {1}, 0),
    FB_STATE("fb:state:{}", "redis.key.fb.state.ttl", new int[] {1}, -1);


    private static final Logger logger = LoggerFactory.getLogger(CacheKey.class);
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\}");
    private static final Properties CACHE_PROPERTIES = new Properties();
    static {
        try {
            final URL cacheResource = CacheKey.class.getResource("/redis.properties");
            if(cacheResource == null) {
                logger.info("No cache key properties file found on classpath.");
            } else {
                logger.info("Found cache key properties file on classpath.  Loading '{}'", cacheResource);
                CACHE_PROPERTIES.load(cacheResource.openStream());
            }
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private final String key;
    private final String ttlProperty;
    private final int[] paramIndices;
    private final int idParamIndex;

    CacheKey(final String key, final String ttlProperty, final int[] paramIndices, final int idParamIndex) {
        this.key = key;
        this.ttlProperty = ttlProperty;
        this.paramIndices = paramIndices;
        this.idParamIndex = idParamIndex;
    }

    public static void reload(final Properties properties) {
        CACHE_PROPERTIES.clear();
        CACHE_PROPERTIES.putAll(properties);
    }

    public static String[] getParameters(final CacheKey cacheKey, final String key) {
        final String[] result = new String[cacheKey.paramIndices.length];
        final String[] tokens = key.split(":");
        for(int i = 0; i < cacheKey.paramIndices.length; i++) {
            result[i] = tokens[cacheKey.paramIndices[i]];
        }
        return result;
    }

    public static String getIdParameter(final CacheKey cacheKey, final String key) {
        if(cacheKey.idParamIndex < 0) {
            throw new IllegalArgumentException(String.format("Cache key %s does not define an ID parameter.", cacheKey.key));
        }
        return getParameters(cacheKey, key)[cacheKey.idParamIndex];
    }

    public String getKey(final Object... values) {

        final int numValues;
        if(values == null) {
            numValues = 0;
        } else {
            numValues = values.length;
        }

        // no substitutions to perform, so return key
        if(numValues == 0) {
            return key;
        }

        final Matcher matcher = VARIABLE_PATTERN.matcher(key);
        final StringBuffer result = new StringBuffer(key.length());

        // replace up to the given number of substitutions
        int count = 0;
        while(matcher.find()) {
            if(count == numValues) {
                break;
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(values[count].toString()));
            count++;
        }

        // if the given values have not been all substituted, throw exception
        if(count < numValues) {
            throw new IllegalArgumentException("Too many values provided for key: " + key);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    public String getTtlProperty() {
        return ttlProperty;
    }

    public int getTtl() {
        final String value = (String) CACHE_PROPERTIES.get(ttlProperty);
        if (value == null) {
            return Integer.MAX_VALUE;
        } else {
            return Integer.parseInt(value);
        }
    }

    public boolean hasTtl() {
        return CACHE_PROPERTIES.get(ttlProperty) != null;
    }
}
