package com.swooli.dao.request;

import java.io.Serializable;

public class VideoSpotlightRequest implements Serializable {

    private long videoVersion;

    private String userProfileId;

    private String videoCollectionId;

    private String videoId;

    private boolean spotlighted;

    public long getVideoVersion() {
        return videoVersion;
    }

    public void setVideoVersion(final long videoVersion) {
        this.videoVersion = videoVersion;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(final String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public boolean isSpotlighted() {
        return spotlighted;
    }

    public void setSpotlighted(final boolean spotlighted) {
        this.spotlighted = spotlighted;
    }

}