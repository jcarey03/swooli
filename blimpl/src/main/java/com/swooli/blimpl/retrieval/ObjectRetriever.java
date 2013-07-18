package com.swooli.blimpl.retrieval;

import com.swooli.ApplicationProperties;
import com.swooli.blimpl.cache.redis.JedisUtil;
import com.swooli.blimpl.cache.redis.scripts.CachedObjectInsertionScript;
import com.swooli.blimpl.cache.redis.scripts.CachedObjectRetrievalScript;
import com.swooli.dao.DaoException;
import com.swooli.serialization.Serializer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

public class ObjectRetriever<T> {

    public static final Charset CHAR_ENCODING = ApplicationProperties.getCharEncoding();

    private static final Logger logger = LoggerFactory.getLogger(ObjectRetriever.class);

    private Jedis cacheConn;

    private Serializer serializer;

    public Collection<T> retrieve(final Collection<ObjectRetrievalArgument<T>> args, final long maxWaitTimeMs,
            final DatabaseObjectRetrievalCallback<T> dbCallback) throws DaoException, JedisException {

        // return fast if no work to do
        if(args.isEmpty()) {
            return new ArrayList<>();
        }

        // a timestamp to use as part of the query marker used when taking ownership of a cache entry
        final long timestamp = new Date().getTime();

        // a map associating object ID with the object for those IDs retrieved from either the cache (preferred) or the database
        final Collection<T> retrievedObjects = new ArrayList<>(args.size());

        // a set of object IDs that other threads own and are responsible for handling in terms of cache population
        final Set<Long> waitingIds = new HashSet<>(args.size());

        /*
         * A set of IDs of objects not currently in the cache and not being handled by other threads.
         * IDs in this set are denoted in the respective cache entries as being owned by this thread.
         * Therefore, this thread is responsible for cleaning up those cache entries before returning
         * from this method.
         */
        final Set<Long> missingIds = new HashSet<>(args.size());

        // the object retrieval script to execute
        final CachedObjectRetrievalScript objectRetrievalScript = new CachedObjectRetrievalScript(cacheConn, serializer);

        // the argument to use for recalling information specific to retrieval
        final ObjectRetrievalArgument<T> prototypeArg = args.iterator().next();

        // a map to associate the object ID to its cache key
        final Map<Long, String> keyMap = new HashMap<>();
        for(final ObjectRetrievalArgument<T> arg : args) {
            keyMap.put(arg.getId(), arg.getKey());
        }

        try {

            // call cache script first time
            objectRetrievalScript.execute(
                prototypeArg.getTargetClass(),
                prototypeArg.getCacheKey(),
                new ArrayList<>(keyMap.values()),
                timestamp,
                retrievedObjects,
                waitingIds,
                missingIds);

            // the timestamp indicating when to stop waiting for the other threads to retrieve the object
            final long maxWait = System.currentTimeMillis() + maxWaitTimeMs;

            while(System.currentTimeMillis() < maxWait && !waitingIds.isEmpty()) {

                try {
                    // sleep a bit
                    Thread.sleep(100);
                } catch(final InterruptedException ie) {
                    break;
                }

                // create the list of cache keys to check for in the cache
                final List<String> currentKeys = new ArrayList<>();
                for(final long waitingId : waitingIds) {
                    currentKeys.add(keyMap.get(waitingId));
                }

                // the set of IDs currently being waited to be populated by other threades
                final Set<Long> currentWaitingIds = new HashSet<>(waitingIds);

                /*
                 * Call script again.  Each call to script partitions the "waiting" IDs into objects
                 * retrieved, "waiting" IDs, and "missing" IDs.
                 */
                objectRetrievalScript.execute(
                    prototypeArg.getTargetClass(),
                    prototypeArg.getCacheKey(),
                    currentKeys,
                    timestamp,
                    retrievedObjects,
                    currentWaitingIds,
                    missingIds);

                // remove the "missing" IDs and the IDs of objects retrieved
                waitingIds.retainAll(currentWaitingIds);
            }

            // retrieve from the database any objects not retrieved in cache
            final Collection<Long> dbObjectIds = new ArrayList<>();
            dbObjectIds.addAll(missingIds);
            dbObjectIds.addAll(waitingIds);
            final Map<Long, T> dbObjects = dbCallback.retrieve(dbObjectIds);

            /*
             * Delete the IDs of objects for which were being waited.  Deletion is preferred over
             * setting the value to prevent race conditions, which could cause an older view of the
             * data to be stored in the cache.
             */
            deleteKeys(keyMap, waitingIds);

            // holds the set of object IDs retrieved from the database and inserted into the cache
            final Set<Long> insertedIds = new HashSet<>();

            // holds the set of object IDs retrieved from the database, but not inserted into the cache because they are stale
            final Set<Long> staleIds = new HashSet<>();

            if(!dbObjects.isEmpty()) {

                // add objects retrieved from database to map of retrieved objects
                retrievedObjects.addAll(dbObjects.values());

                // the object insertion script to execute
                final CachedObjectInsertionScript objectInsertionScript = new CachedObjectInsertionScript(cacheConn, serializer);
                objectInsertionScript.execute(
                    prototypeArg.getTargetClass(),
                    prototypeArg.getCacheKey(),
                    dbObjects,
                    JedisUtil.getQueryFlag(timestamp, /* withPrefix */ true),
                    insertedIds,
                    staleIds);
            }

            // remove the IDs of objects found in the database
            missingIds.removeAll(dbObjects.keySet());

            // reset ttl for retrieved objects
            final Collection<Long> retrievedIds = new ArrayList<>(keyMap.keySet());
            retrievedIds.removeAll(missingIds);
            final Pipeline resetTtlPipeline = cacheConn.pipelined();
            for(final long retrievedId : retrievedIds) {
                resetTtlPipeline.expire(keyMap.get(retrievedId).getBytes(CHAR_ENCODING), prototypeArg.getCacheKey().getTtl());
            }
            resetTtlPipeline.sync();

            return retrievedObjects;

        } finally {
            try {
                deleteKeys(keyMap, missingIds);
            } catch(final JedisException je) {
                logger.warn("Failed to delete cache keys marked to be queried due to {}.  Cache will be inconsistent until keys expire.", je);
            }
        }
    }

    public Jedis getCacheConn() {
        return cacheConn;
    }

    public void setCacheConn(final Jedis cacheConn) {
        this.cacheConn = cacheConn;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(final Serializer serializer) {
        this.serializer = serializer;
    }

    private void deleteKeys(final Map<Long, String> keyMap, final Collection<Long> ids) throws JedisException {

        if(ids.isEmpty()) {
            return;
        }

        final byte[][] idKeys = new byte[ids.size()][];
        int idx = 0;
        for(final long id : ids) {
            idKeys[idx] = keyMap.get(id).getBytes(CHAR_ENCODING);
            idx++;
        }

        cacheConn.del(idKeys);

    }
}