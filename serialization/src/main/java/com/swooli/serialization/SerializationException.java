package com.swooli.serialization;

/**
 * Base exception class for serialization exceptions.
 *
 * @author jmcarey
 */
public class SerializationException extends RuntimeException {

    public SerializationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SerializationException(final Throwable cause) {
        super(cause);
    }

    public SerializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SerializationException(final String message) {
        super(message);
    }

    public SerializationException() {
    }

}