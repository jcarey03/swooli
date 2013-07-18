package com.swooli.security;

public class EncryptionException extends RuntimeException {

    public EncryptionException() {
    }

    public EncryptionException(final String message) {
        super(message);
    }

    public EncryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EncryptionException(final Throwable cause) {
        super(cause);
    }

    public EncryptionException(final String message, final Throwable cause, final boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}