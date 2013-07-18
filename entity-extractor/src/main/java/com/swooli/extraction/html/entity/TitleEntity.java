package com.swooli.extraction.html.entity;

import com.swooli.extraction.html.entity.HtmlEntity.EntityType;

public class TitleEntity extends AbstractHtmlEntity implements HtmlEntity {

    private String title;

    public TitleEntity(final String origin) {
        super(origin);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        strb.append(getType()).append(" title=").append(title);
        return strb.toString();
    }

    @Override
    public EntityType getType() {
        return EntityType.TITLE;
    }
}