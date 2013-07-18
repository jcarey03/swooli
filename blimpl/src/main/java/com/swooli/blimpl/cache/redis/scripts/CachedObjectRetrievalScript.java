package com.swooli.blimpl.cache.redis.scripts;

import com.swooli.ApplicationProperties;
import com.swooli.blimpl.cache.redis.CacheKey;
import com.swooli.blimpl.cache.redis.JedisUtil;
import com.swooli.serialization.SerializationException;
import com.swooli.serialization.Serializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class CachedObjectRetrievalScript {

    public static final Charset CHAR_ENCODING = ApplicationProperties.getCharEncoding();

    public static final String CACHED_OBJECT_RETRIEVAL_SCRIPT_RSC = "/lua/cached-object-retrieval.lua";
    public static final String CACHED_OBJECT_RETRIEVAL_SCRIPT;
    static {
        try {
            CACHED_OBJECT_RETRIEVAL_SCRIPT = IOUtils.toString(CachedObjectRetrievalScript.class.getResourceAsStream(CACHED_OBJECT_RETRIEVAL_SCRIPT_RSC));
        } catch(final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    public static final String CACHED_OBJECT_RETRIEVAL_SCRIPT_HASH = DigestUtils.shaHex(CACHED_OBJECT_RETRIEVAL_SCRIPT);

    private static final Logger logger = LoggerFactory.getLogger(CachedObjectRetrievalScript.class);

    private Jedis connection;

    private Serializer serializer;

    public CachedObjectRetrievalScript(final Jedis connection, final Serializer serializer) {
        this.connection = connection;
        this.serializer = serializer;
    }

    public static void loadScript(final Jedis connection) throws JedisException {
        connection.scriptLoad(CACHED_OBJECT_RETRIEVAL_SCRIPT);
    }

    public <T> void execute(final Class<T> clazz, final CacheKey cacheKey, final List<String> keys, final long timestamp,
            final Collection<T> retrievedObjects, final Set<Long> waitingIds, final Set<Long> missingIds) throws JedisException {

        final List<List<String>> scriptResponse = (List<List<String>>) connection.evalsha(
            CACHED_OBJECT_RETRIEVAL_SCRIPT_HASH,
            keys,
            Arrays.asList(String.valueOf(cacheKey.getTtl()), JedisUtil.QUERY_FLAG_PREFIX, JedisUtil.getQueryFlag(timestamp, false))
        );

        if(retrievedObjects != null) {
            // process the serialized objects found in the cache
            for(final String cachedCollection : scriptResponse.get(0)) {
                try {
                    final T object = serializer.deserialize(clazz, new ByteArrayInputStream(cachedCollection.getBytes(CHAR_ENCODING)));
                    retrievedObjects.add(object);
                } catch(final SerializationException se) {
                    logger.warn("Failed deserialization of type {} due to {} ", new Object[] {clazz, se});
                }
            }
        }

        if(waitingIds != null) {
            // get the IDs of the objects currently being queried by other threads
            for(final String objectKey : scriptResponse.get(1)) {
                try {
                    final String objectId = CacheKey.getIdParameter(cacheKey, objectKey);
                    waitingIds.add(Long.valueOf(objectId));
                } catch(final NumberFormatException nfe) {
                    logger.warn("Failed numeric parsing of (waiting) key {} due to {}", new Object[] {objectKey, nfe});
                }
            }
        }

        if(missingIds != null) {
            // get the IDs of objects not in the cache
            for(final String objectKey : scriptResponse.get(1)) {
                try {
                    final String objectId = CacheKey.getIdParameter(cacheKey, objectKey);
                    missingIds.add(Long.valueOf(objectId));
                } catch(final NumberFormatException nfe) {
                    logger.warn("Failed numeric parsing of (missing) key {} due to {}", new Object[] {objectKey, nfe});
                }
            }
        }

    }
}