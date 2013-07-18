package com.swooli.extraction.html.media;

public class ImageInfo {

    private int width;

    private int height;

    private String origin;

    private String contentType;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public double getAspectRatio() {
        if(width == 0 || height == 0) {
            return 0;
        } else if(width >= height) {
            return (double) width / height;
        } else {
            return (double) height / width;
        }
    }
}