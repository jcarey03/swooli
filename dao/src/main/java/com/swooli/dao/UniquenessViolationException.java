package com.swooli.dao;

public class UniquenessViolationException extends DaoException {

    public UniquenessViolationException(final String message, final Throwable cause, final boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UniquenessViolationException(final Throwable cause) {
        super(cause);
    }

    public UniquenessViolationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UniquenessViolationException(final String message) {
        super(message);
    }

    public UniquenessViolationException() {
    }

}