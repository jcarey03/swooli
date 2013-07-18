package com.swooli.blimpl.cache.redis;

import com.swooli.blimpl.cache.redis.scripts.CachedObjectInsertionScript;
import com.swooli.blimpl.cache.redis.scripts.CachedObjectRetrievalScript;
import com.swooli.blimpl.cache.redis.scripts.CopyKeyScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public final class JedisUtil {

    public static final String QUERY_FLAG_PREFIX = "query--";

    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    public static void loadScripts(final Jedis connection) throws JedisException {
        CopyKeyScript.loadScript(connection);
        CachedObjectRetrievalScript.loadScript(connection);
        CachedObjectInsertionScript.loadScript(connection);
    }

    public static String getQueryFlag(final long timestamp, final boolean withPrefix) {

        final Thread currentThread = Thread.currentThread();
        final StringBuilder strb = new StringBuilder();

        if(withPrefix) {
            strb.append(QUERY_FLAG_PREFIX);
        }

        strb
            .append(currentThread.getThreadGroup().getName())
            .append("::")
            .append(currentThread.getName())
            .append("::")
            .append(timestamp);

        return strb.toString();
    }

}