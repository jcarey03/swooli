package com.swooli.dao.request;

import com.swooli.bo.video.Swink;
import java.io.Serializable;

public class SwinkCreateRequest implements Serializable {

    private String videoCollectionId;

    private long videoRootVersion;

    private Swink swink;

    public Swink getSwink() {
        return swink;
    }

    public void setSwink(final Swink swink) {
        this.swink = swink;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public long getVideoRootVersion() {
        return videoRootVersion;
    }

    public void setVideoRootVersion(final long videoRootVersion) {
        this.videoRootVersion = videoRootVersion;
    }

}