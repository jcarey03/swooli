package com.swooli.dao;

/**
 * Represents an exceptional case when performing data access operations.
 *
 * @author jmcarey
 */
public class OptimisticLockingException extends DaoException{

    public OptimisticLockingException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OptimisticLockingException(final Throwable cause) {
        super(cause);
    }

    public OptimisticLockingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockingException(final String message) {
        super(message);
    }

    public OptimisticLockingException() {
    }

    /**
     * A factory method representing the exceptional case when an object is not found.
     *
     * @param clazz the class type of the object
     * @param id the suspect id
     *
     * @return the exception
     */
    public static OptimisticLockingException objectNotFound(final Class<?> clazz, final long id) {
        return new OptimisticLockingException(String.format("Object of type '%s' not found: '%s'", clazz.getName(), id));
    }

    /**
     * A factory method representing the exceptional case when an object is not found.
     *
     * @param clazz the class type of the object
     * @param id the suspect id
     * @param entity a friendly name to better describe the domain object and aid in error messaging
     *
     * @return the exception
     */
    public static OptimisticLockingException objectNotFound(final Class<?> clazz, final long id, final String entity) {
        if(entity == null) {
            return objectNotFound(clazz, id);
        } else {
            return new OptimisticLockingException(String.format("Entity '%s' of type '%s' not found: '%s'", entity, clazz.getName(), id));
        }
    }

}