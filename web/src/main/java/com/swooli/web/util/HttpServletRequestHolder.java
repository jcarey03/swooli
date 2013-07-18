package com.swooli.web.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestHolder {

    private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(final HttpServletRequest request) {
        THREAD_LOCAL.set(request);
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }

    public static HttpServletRequest get() {
        return THREAD_LOCAL.get();
    }

}