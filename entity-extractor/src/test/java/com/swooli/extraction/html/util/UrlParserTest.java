package com.swooli.extraction.html.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UrlParserTest {

    private UrlParser parser;

    @Before
    public void setup() {
        parser = new UrlParser();
    }

    @Test
    public void testParseUrlHost() {
        final UrlInfo info = parser.parseUrl("http://www.google.com");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertNull(info.getPath());
        assertNull(info.getFragment());
    }

    @Test
    public void testParseUrlSecureHost() {
        final UrlInfo info = parser.parseUrl("https://www.google.com");
        assertEquals("https", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertNull(info.getPath());
        assertNull(info.getQuery());
        assertNull(info.getFragment());
    }

    @Test
    public void testParseUrlHostTrailingSlash() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/", info.getPath());
        assertNull(info.getQuery());
        assertNull(info.getFragment());
    }

    @Test
    public void testParseUrlEmptyQuery() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/?");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/", info.getPath());
        assertEquals("", info.getQuery());
        assertNull(info.getFragment());
    }

    @Test
    public void testParseUrlEmptyFragment() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/#");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/", info.getPath());
        assertNull(info.getQuery());
        assertEquals("", info.getFragment());
    }

    @Test
    public void testParseUrlEmptyQueryAndEmptyFragment() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/?#");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/", info.getPath());
        assertEquals("", info.getQuery());
        assertEquals("", info.getFragment());
    }

    @Test
    public void testParseUrlPath() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/foo/bar");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/foo/bar", info.getPath());
        assertNull(info.getQuery());
        assertNull(info.getFragment());
    }

    @Test
    public void testParseUrlQueryAndFragment() {
        final UrlInfo info = parser.parseUrl("http://www.google.com/foo/bar?a=b&c=d#fragment");
        assertEquals("http", info.getScheme());
        assertEquals("www.google.com", info.getHost());
        assertEquals("/foo/bar", info.getPath());
        assertEquals("a=b&c=d", info.getQuery());
        assertEquals("fragment", info.getFragment());
    }

    @Test
    public void testParseQuery() {
        final String query = "a=b&c=d";
        final StringMultivaluedMap params = parser.parseQuery(query);
        assertEquals(2, params.size());
        final Iterator<Entry<String, List<String>>> itr = params.entrySet().iterator();
        Entry<String, List<String>> entry;

        entry = itr.next();
        assertEquals("b", params.get("a").iterator().next());

        entry = itr.next();
        assertEquals("d", params.get("c").iterator().next());
    }

    @Test
    public void testParseQueryMultivalued() {
        final String query = "a=b&c=d&a=z";
        final StringMultivaluedMap params = parser.parseQuery(query);
        assertEquals(2, params.size());
        final Iterator<Entry<String, List<String>>> itr = params.entrySet().iterator();
        Entry<String, List<String>> entry;

        entry = itr.next();
        final Iterator<String> itr2 = params.get("a").iterator();
        assertEquals("b", itr2.next());
        assertEquals("z", itr2.next());

        entry = itr.next();
        assertEquals("d", params.get("c").iterator().next());
    }

    @Test
    public void testParseQueryMissingValue() {
        final String query = "a=b&c=";
        final StringMultivaluedMap params = parser.parseQuery(query);
        assertEquals(2, params.size());
        final Iterator<Entry<String, List<String>>> itr = params.entrySet().iterator();
        Entry<String, List<String>> entry;

        entry = itr.next();
        assertEquals("b", params.get("a").iterator().next());

        entry = itr.next();
        assertEquals("", params.get("c").iterator().next());
    }

    @Test
    public void testParseQueryEmpty() {
        final String query = "";
        final StringMultivaluedMap params = parser.parseQuery(query);
        assertTrue(params.isEmpty());
    }

}