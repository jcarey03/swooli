package com.swooli.security;

import com.swooli.bo.user.UserProfile;

/**
 * A threadlocal holder for passing the user session along the call chain.
 * 
 * @author jmcarey
 */
public class UserSessionContextHolder {

    public static final ThreadLocal<UserSessionContext> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(UserSessionContext ctx) {
        THREAD_LOCAL.set(ctx);
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }

    public static UserSessionContext get() {
        return THREAD_LOCAL.get();
    }

    public static UserProfile getUserProfile() {
        final UserSessionContext ctx = get();
        if(ctx == null) {
            return null;
        } else {
            return ctx.getUserProfile();
        }
    }

}