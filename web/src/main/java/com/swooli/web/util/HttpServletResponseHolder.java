package com.swooli.web.util;

import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseHolder {

    private static final ThreadLocal<HttpServletResponse> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(final HttpServletResponse request) {
        THREAD_LOCAL.set(request);
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }

    public static HttpServletResponse get() {
        return THREAD_LOCAL.get();
    }

}