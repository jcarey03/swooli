package com.swooli.dao.request;

import com.swooli.bo.video.VideoPop;

public class VideoPopRequest {

    private long toVideoCollectionVersion;

    private long videoRootVersion;

    private VideoPop pop;

    public long getVideoRootVersion() {
        return videoRootVersion;
    }

    public void setVideoRootVersion(final long videoRootVersion) {
        this.videoRootVersion = videoRootVersion;
    }

    public long getToVideoCollectionVersion() {
        return toVideoCollectionVersion;
    }

    public void setToVideoCollectionVersion(final long toVideoCollectionVersion) {
        this.toVideoCollectionVersion = toVideoCollectionVersion;
    }

    public VideoPop getPop() {
        return pop;
    }

    public void setPop(final VideoPop pop) {
        this.pop = pop;
    }

}