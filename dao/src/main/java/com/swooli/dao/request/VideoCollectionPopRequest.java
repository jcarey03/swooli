package com.swooli.dao.request;

import com.swooli.bo.video.collection.VideoCollectionPop;

public class VideoCollectionPopRequest {

    private long userProfileVersion;

    private long videoCollectionVersion;

    private VideoCollectionPop pop;

    public long getVideoCollectionVersion() {
        return videoCollectionVersion;
    }

    public void setVideoCollectionVersion(final long videoCollectionVersion) {
        this.videoCollectionVersion = videoCollectionVersion;
    }

    public VideoCollectionPop getPop() {
        return pop;
    }

    public void setPop(final VideoCollectionPop pop) {
        this.pop = pop;
    }

    public long getUserProfileVersion() {
        return userProfileVersion;
    }

    public void setUserProfileVersion(final long userProfileVersion) {
        this.userProfileVersion = userProfileVersion;
    }

}