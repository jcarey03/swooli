package com.swooli.dao.request;

import com.swooli.bo.video.VideoMetadata;
import java.io.Serializable;

public class VideoCreateRequest implements Serializable {

    private long videoCollectionVersion;

    private VideoMetadata videoMetadata;

    public long getVideoCollectionVersion() {
        return videoCollectionVersion;
    }

    public void setVideoCollectionVersion(final long videoCollectionVersion) {
        this.videoCollectionVersion = videoCollectionVersion;
    }

    public VideoMetadata getVideoMetadata() {
        return videoMetadata;
    }

    public void setVideoMetadata(final VideoMetadata videoMetadata) {
        this.videoMetadata = videoMetadata;
    }

}