package com.swooli.dao;

/**
 * Represents an exceptional case when performing data access operations.
 *
 * @author jmcarey
 */
public class ObjectNotFoundException extends DaoException{

    public ObjectNotFoundException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ObjectNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(final String message) {
        super(message);
    }

    public ObjectNotFoundException() {
    }

}