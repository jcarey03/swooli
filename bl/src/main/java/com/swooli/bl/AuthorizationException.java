package com.swooli.bl;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(final String message) {
        super(message);
    }

    public AuthorizationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(final Throwable cause) {
        super(cause);
    }

    public AuthorizationException(final String message, final Throwable cause, final boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}