package com.swooli.dao.request;

import java.io.Serializable;

public class VideoUpdateRequest implements Serializable {

    private String videoCollectionId;

    private String videoId;

    private long videoVersion;

    private String title;

    private Boolean spotlighted;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public Boolean isSpotlighted() {
        return spotlighted;
    }

    public void setSpotlighted(final Boolean spotlighted) {
        this.spotlighted = spotlighted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public long getVideoVersion() {
        return videoVersion;
    }

    public void setVideoVersion(final long videoVersion) {
        this.videoVersion = videoVersion;
    }

}