package com.swooli.blimpl.cache.redis.scripts;

import com.swooli.ApplicationProperties;
import com.swooli.blimpl.cache.redis.CacheKey;
import com.swooli.serialization.SerializationException;
import com.swooli.serialization.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class CachedObjectInsertionScript {

    public static final Charset CHAR_ENCODING = ApplicationProperties.getCharEncoding();

    public static final String CACHED_OBJECT_INSERTION_SCRIPT_RSC = "/lua/cached-object-insertion.lua";
    public static final String CACHED_OBJECT_INSERTION_SCRIPT;
    static {
        try {
            CACHED_OBJECT_INSERTION_SCRIPT = IOUtils.toString(CachedObjectInsertionScript.class.getResourceAsStream(CACHED_OBJECT_INSERTION_SCRIPT_RSC));
        } catch(final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    public static final String CACHED_OBJECT_INSERTION_SCRIPT_HASH = DigestUtils.shaHex(CACHED_OBJECT_INSERTION_SCRIPT);

    private static final Logger logger = LoggerFactory.getLogger(CachedObjectInsertionScript.class);

    private Jedis connection;

    private Serializer serializer;

    public CachedObjectInsertionScript(final Jedis connection, final Serializer serializer) {
        this.connection = connection;
        this.serializer = serializer;
    }

    public static void loadScript(final Jedis connection) throws JedisException {
        connection.scriptLoad(CACHED_OBJECT_INSERTION_SCRIPT);
    }

    public <T> void execute(final Class<T> clazz, final CacheKey cacheKey, final Map<Long, T> objects,
            final String queryMarker, final Set<Long> insertedIds, final Set<Long> staleIds) throws JedisException {

        final List<String> keys = new ArrayList<>();
        final List<String> scriptArgs = new ArrayList<>();
        scriptArgs.add(String.valueOf(cacheKey.getTtl()));
        scriptArgs.add(queryMarker);

        final ByteArrayOutputStream serializedOs = new ByteArrayOutputStream(500);
        for(final Map.Entry<Long, T> entry : objects.entrySet()) {
            try {
                serializer.serialize(entry.getValue(), clazz, serializedOs);
                scriptArgs.add(serializedOs.toString(CHAR_ENCODING.name()));
                keys.add(cacheKey.getKey(entry.getKey()));
            } catch(final SerializationException se) {
                logger.warn("Failed to serialize object of type {} due to {}", new Object[] {clazz, se});
            } catch(final UnsupportedEncodingException uee) {
                logger.warn("Unsupported encoding {} due to {}", new Object[] {CHAR_ENCODING, uee});
            }
        }

        final List<List<String>> scriptResponse = (List<List<String>>) connection.evalsha(
            CACHED_OBJECT_INSERTION_SCRIPT_HASH,
            keys,
            scriptArgs
        );

        if(insertedIds != null) {
            // get the IDs of the objects inserted
            for(final String objectKey : scriptResponse.get(0)) {
                try {
                    final String objectId = CacheKey.getIdParameter(cacheKey, objectKey);
                    insertedIds.add(Long.valueOf(objectId));
                } catch(final NumberFormatException nfe) {
                    logger.warn("Failed numeric parsing of (inserted) key {} due to {}", new Object[] {objectKey, nfe});
                }
            }
        }

        if(staleIds != null) {
            // get the IDs of stale objects
            for(final String objectKey : scriptResponse.get(1)) {
                try {
                    final String objectId = CacheKey.getIdParameter(cacheKey, objectKey);
                    staleIds.add(Long.valueOf(objectId));
                } catch(final NumberFormatException nfe) {
                    logger.warn("Failed numeric parsing of (stale) key {} due to {}", new Object[] {objectKey, nfe});
                }
            }
        }

    }
}