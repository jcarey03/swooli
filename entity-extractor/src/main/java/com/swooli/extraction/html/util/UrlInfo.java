package com.swooli.extraction.html.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlInfo {

    private String scheme;

    private String host;

    private String path;

    private String query;

    private String fragment;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(final String fragment) {
        this.fragment = fragment;
    }

    public URI toUri() throws URISyntaxException {
        return new URI(scheme, host, path, query, fragment);
    }

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        strb.append(scheme).append("://").append(host);
        if(path != null) {
            strb.append(path);
        }
        if(query != null) {
            strb.append("?").append(query);
        }
        if(fragment != null) {
            strb.append("#").append(fragment);
        }
        return strb.toString();
    }

}