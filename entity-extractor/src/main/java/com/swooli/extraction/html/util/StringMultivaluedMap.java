package com.swooli.extraction.html.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringMultivaluedMap implements MultivaluedMap<String, String> {

    private Map<String, List<String>> map = new HashMap<>();

    @Override
    public void putSingle(final String key, final String value) {
        List<String> values = get(key);
        if(values == null) {
            values = new ArrayList<>();
            put(key, values);
        } else {
            values.clear();
        }
        values.add(value);
    }

    @Override
    public void add(final String key, final String value) {
        List<String> values = get(key);
        if(values == null) {
            values = new ArrayList<>();
            put(key, values);
        }
        values.add(value);
    }

    @Override
    public String getFirst(final String key) {
        final List<String> values = get(key);
        if(values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public List<String> get(final Object key) {
        return map.get(key);
    }

    @Override
    public List<String> put(final String key, final List<String> value) {
        return map.put(key, value);
    }

    @Override
    public List<String> remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends List<String>> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

}