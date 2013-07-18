package com.swooli.bo.video;

import java.io.Serializable;
import java.net.URI;

public class VideoImpression implements Serializable {

    private String id;

    private URI thumbnailUri;

    public VideoImpression() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public URI getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(final URI thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

}