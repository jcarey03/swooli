package com.swooli.extraction.html.entity;

import com.swooli.extraction.html.entity.HtmlEntity.EntityType;

public class MetaEntity extends AbstractHtmlEntity implements HtmlEntity {

    private String property;

    private String content;

    public MetaEntity(final String origin) {
        super(origin);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(final String name) {
        this.property = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        strb.append(getType()).append(" property=").append(property).append(", content=").append(content);
        return strb.toString();
    }

    @Override
    public EntityType getType() {
        return EntityType.META;
    }
}