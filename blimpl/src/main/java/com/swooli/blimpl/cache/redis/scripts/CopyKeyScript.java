package com.swooli.blimpl.cache.redis.scripts;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class CopyKeyScript {

    public static Charset UTF8 = Charset.forName("utf8");

    public static final String COPY_KEY_SCRIPT_RSC = "/lua/copy-key.lua";
    public static final String COPY_KEY_SCRIPT;
    static {
        try {
            COPY_KEY_SCRIPT = IOUtils.toString(CopyKeyScript.class.getResourceAsStream(COPY_KEY_SCRIPT_RSC));
        } catch(final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    public static final String COPY_KEY_SCRIPT_HASH = DigestUtils.shaHex(COPY_KEY_SCRIPT);

    private static final Logger logger = LoggerFactory.getLogger(CopyKeyScript.class);

    private Jedis connection;

    public CopyKeyScript(final Jedis connection) {
        this.connection = connection;
    }

    public static void loadScript(final Jedis connection) throws JedisException {
        connection.scriptLoad(COPY_KEY_SCRIPT);
    }

    public Object execute(final String srcKey, final String destKey) throws JedisException {
        return connection.evalsha(COPY_KEY_SCRIPT_HASH, Arrays.asList(srcKey, destKey), new ArrayList<String>());
    }

    public Object executeQuietly(final String srcKey, final String destKey) {
        try {
            return connection.evalsha(COPY_KEY_SCRIPT_HASH, Arrays.asList(srcKey, destKey), new ArrayList<String>());
        } catch(final JedisException je) {
            logger.debug("{} encountered exception due to {}", new Object[] {CopyKeyScript.class, je});
            return null;
        }
    }
}