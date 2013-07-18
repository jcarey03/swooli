package com.swooli.extraction.html.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HtmlUtil {

    private static final Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

    public static String stripOpenGraphNamespacePrefix(final String value) {
        // assumed format: <open graph prefix>:<open graph entity>
        final int index = value.indexOf(":");
        if(index < 0) {                         // no colon
            return value;
        } else if(value.length() == index) {    // value ends in colon
            return value;
        } else {                                // strip prefix
            return value.substring(index + 1);
        }
    }

    public static String toAbsolute(final String origin, final String src) {

        final UrlParser parser = new UrlParser();
        final UrlInfo originInfo = parser.parseUrl(origin);

        final StringBuilder strb = new StringBuilder();
        if(src.startsWith("http://") || src.startsWith("https://")) {
            return src;
        } else if(src.startsWith("//")) {
            strb.append(originInfo.getScheme()).append(":").append(src);
        } else if(src.startsWith("/")) {
            strb.append(originInfo.getScheme()).append("://").append(originInfo.getHost()).append(src);
        } else {
            final UrlInfo originMinusLastPath = parser.trimLastPath(originInfo);
            strb.append(originMinusLastPath.getScheme()).append("://").append(originMinusLastPath.getHost());
            if(originMinusLastPath.getPath() != null) {
                strb.append(originMinusLastPath.getPath());
            }
            strb.append("/").append(src);
        }

        return strb.toString();

    }

}