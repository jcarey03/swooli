package com.swooli.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A annotation denoting that that a user must be authenticated before performing the annotated
 * operation.
 *
 * If the user is not authenticated, then the user should be redirected to the login page.  Upon
 * successful authentication, the user should be redirected back to the originating request.
 *
 * @author jmcarey
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticated {}

