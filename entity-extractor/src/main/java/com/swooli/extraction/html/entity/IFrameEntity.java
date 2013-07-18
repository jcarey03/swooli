package com.swooli.extraction.html.entity;

import com.swooli.extraction.html.entity.HtmlEntity.EntityType;

public class IFrameEntity extends AbstractHtmlEntity implements HtmlEntity {

    private String src;

    public IFrameEntity(final String origin) {
        super(origin);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(final String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        strb.append(getType()).append(", src=").append(src);
        return strb.toString();
    }

    @Override
    public EntityType getType() {
        return EntityType.IFRAME;
    }

}