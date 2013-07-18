package com.swooli.blimpl;

import com.swooli.bl.FacebookService;
import com.swooli.blimpl.cache.redis.CacheKey;
import com.swooli.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

public class FacebookServiceImpl implements FacebookService {

    private static Logger logger = LoggerFactory.getLogger(FacebookServiceImpl.class);

    private JedisPool connectionPool;

    @Override
    public void createLoginState(final String stateId, final String state) throws DaoException {
        Jedis connection = null;
        try {
            connection = connectionPool.getResource();
            connection.setex(CacheKey.FB_STATE.getKey(stateId), CacheKey.FB_STATE.getTtl(), state);
            connectionPool.returnResource(connection);
        } catch(final JedisException je) {
            try {
                if(connection != null) {
                    connectionPool.returnBrokenResource(connection);
                }
            } catch (final JedisException je2) {
                logger.warn("Failed returning broken jedis connection to pool", je2);
            }
            throw new DaoException(je);
        }
    }

    @Override
    public String retrieveLoginState(final String stateId) throws DaoException {
        Jedis connection = null;
        try {
            connection = connectionPool.getResource();
            final String result = connection.get(CacheKey.FB_STATE.getKey(stateId));
            connectionPool.returnResource(connection);
            return result;
        } catch(final JedisException je) {
            try {
                if(connection != null) {
                    connectionPool.returnBrokenResource(connection);
                }
            } catch (final JedisException je2) {
                logger.warn("Failed returning broken jedis connection to pool", je2);
            }
            throw new DaoException(je);
        }
    }

    @Override
    public void deleteLoginState(final String stateId) throws DaoException {
        Jedis connection = null;
        try {
            connection = connectionPool.getResource();
            connection.del(CacheKey.FB_STATE.getKey(stateId));
            connectionPool.returnResource(connection);
        } catch(final JedisException je) {
            try {
                if(connection != null) {
                    connectionPool.returnBrokenResource(connection);
                }
            } catch (final JedisException je2) {
                logger.warn("Failed returning broken jedis connection to pool", je2);
            }
            throw new DaoException(je);
        }
    }

    public void setConnectionPool(final JedisPool connectionPool) {
        this.connectionPool = connectionPool;
    }

}