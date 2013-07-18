package com.swooli.extraction.html.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class UrlParser {

    public static String combinePaths(final String... paths) {

        if(paths == null) {
            return null;
        }

        final StringBuilder pathBuilder = new StringBuilder();
        for(int i = 0; i < paths.length; i++) {
            if(pathBuilder.length() == 0) {
                final String pathToAdd = (paths[i].endsWith("/")) ? paths[i].substring(0, paths[i].length() - 1) : paths[i];
                pathBuilder.append(pathToAdd);
            } else {
                final String pathToAdd = (paths[i].startsWith("/")) ? paths[i] : "/" + paths[i];
                pathBuilder.append(pathToAdd);
            }
        }

        return pathBuilder.toString();
    }

    public UrlInfo parseUrl(final String url) {

        final UrlInfo info = new UrlInfo();

        final int doubleSlashIndex = url.indexOf("//");
        if(doubleSlashIndex < 0) {
            return info;
        }

        info.setScheme(url.substring(0, doubleSlashIndex - 1));

        final int firstSlashIndex = url.indexOf("/", doubleSlashIndex + 2);
        if(firstSlashIndex < 0) {
            info.setHost(url.substring(doubleSlashIndex + 2));
            return info;
        } else {
            info.setHost(url.substring(doubleSlashIndex + 2, firstSlashIndex));
        }

        final int lastSlashIndex = url.lastIndexOf("/", firstSlashIndex);
        final int firstQuestionMarkIndex = url.indexOf("?", lastSlashIndex);
        final int firstHashIndex = url.indexOf("#", lastSlashIndex);

        if(firstQuestionMarkIndex < 0 && firstHashIndex < 0) {
            info.setPath(url.substring(lastSlashIndex));
        } else if(firstQuestionMarkIndex >= 0) {
            info.setPath(url.substring(lastSlashIndex, firstQuestionMarkIndex));
        } else if(firstHashIndex >= 0) {
            info.setPath(url.substring(lastSlashIndex, firstHashIndex));
        }

        if(firstQuestionMarkIndex >= 0) {
            if(firstHashIndex > 0) {
                info.setQuery(url.substring(firstQuestionMarkIndex + 1, firstHashIndex));
            } else {
                info.setQuery(url.substring(firstQuestionMarkIndex + 1));
            }
        }

        if(firstHashIndex >= 0) {
            info.setFragment(url.substring(firstHashIndex + 1));
        }

        return info;
    }

    public StringMultivaluedMap parseQuery(final String query) {

        final StringMultivaluedMap params = new StringMultivaluedMap();

        if(StringUtils.isBlank(query)) {
            return params;
        }

        final String[] kvPairs = query.split("&");
        for(final String kvPair : kvPairs) {
            final String[] keyValue = kvPair.split("=");
            if(keyValue.length == 2) {
                params.add(keyValue[0], keyValue[1]);
            } else {
                params.add(keyValue[0], "");
            }
        }

        return params;
    }

    public String toQuery(final StringMultivaluedMap params) {

        if(params == null) {
            return null;
        } else if(params.isEmpty()) {
            return "";
        }

        final StringBuilder strb = new StringBuilder();
        for(final Map.Entry<String, List<String>> entry : params.entrySet()) {
            strb.append(entry.getKey()).append("=");
            if(!entry.getValue().isEmpty()) {
                for(final String value : entry.getValue()) {
                    strb.append(value).append(entry.getKey()).append("=");
                }
                strb.delete(strb.length() - entry.getKey().length() - 1, strb.length());
            }
            strb.append("&");
        }

        strb.delete(strb.length() - 1, strb.length());

        return strb.toString();
    }

    public UrlInfo removeTrailingSlash(final UrlInfo urlInfo) {
        final String path = urlInfo.getPath();
        if(path != null && path.endsWith("/")) {
            urlInfo.setPath(path.substring(0, path.length() - 1));
        }
        return urlInfo;
    }

    public UrlInfo retainQueryParameters(final UrlInfo urlInfo, final Collection<String> keys) {

        final String query = urlInfo.getQuery();
        if(StringUtils.isBlank(query)) {
            return urlInfo;
        }

        final StringMultivaluedMap params = parseQuery(query);
        params.keySet().retainAll(keys);

        urlInfo.setQuery(toQuery(params));

        return urlInfo;
    }

    public UrlInfo removeQueryParameters(final UrlInfo urlInfo, final Collection<String> keys) {

        final String query = urlInfo.getQuery();
        if(StringUtils.isBlank(query)) {
            return urlInfo;
        }

        final StringMultivaluedMap params = parseQuery(query);
        params.keySet().removeAll(keys);

        urlInfo.setQuery(toQuery(params));

        return urlInfo;
    }

    public UrlInfo trimLastPath(final UrlInfo urlInfo) {

        final String path = urlInfo.getPath();
        if(StringUtils.isBlank(path) || path.length() == 1) {
            return urlInfo;
        }

        final int index = path.lastIndexOf("/");
        urlInfo.setPath(path.substring(0, index));
        return urlInfo;

    }
}