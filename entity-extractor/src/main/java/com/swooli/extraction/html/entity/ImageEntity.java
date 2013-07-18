package com.swooli.extraction.html.entity;

import com.swooli.extraction.html.entity.HtmlEntity.EntityType;

public class ImageEntity extends AbstractHtmlEntity implements HtmlEntity {

    private String src;

    private int width;

    private int height;

    public ImageEntity(final String origin) {
        super(origin);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(final String src) {
        this.src = src;
    }

    @Override
    public EntityType getType() {
        return EntityType.IMAGE;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        strb.append(getType()).append(" src=").append(src);
        if(width > 0) {
            strb.append(", width=").append(width);
        }
        if(height > 0) {
            strb.append(", height=").append(height);
        }
        return strb.toString();
    }

}