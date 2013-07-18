package com.swooli.extraction.html.entity;

public abstract class AbstractHtmlEntity implements HtmlEntity {

    private String origin;

    public AbstractHtmlEntity(final String origin) {
        this.origin = origin;
    }

    @Override
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

}