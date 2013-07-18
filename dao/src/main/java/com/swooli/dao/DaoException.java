package com.swooli.dao;

/**
 * Represents an exceptional case when performing data access operations.
 *
 * @author jmcarey
 */
public class DaoException extends RuntimeException{

    public DaoException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DaoException(final Throwable cause) {
        super(cause);
    }

    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DaoException(final String message) {
        super(message);
    }

    public DaoException() {
    }

}