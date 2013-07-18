package com.swooli.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class WebUtils {

    public static String encodeUrl(final String url) {
        try {
            return URLEncoder.encode(url, "utf8");
        } catch(final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }

    public static String decodeUrl(final String url) {
        try {
            return URLDecoder.decode(url, "utf8");
        } catch(final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }
    }

}