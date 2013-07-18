package com.swooli.extraction.html.entity;

public interface HtmlEntity {

    public static enum EntityType {
        META,
        META_LIST,
        IFRAME,
        EMBED,
        TITLE,
        IMAGE
    }

    EntityType getType();

    String getOrigin();

}
