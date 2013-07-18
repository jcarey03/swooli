package com.swooli.bo.video.collection;

import com.swooli.bo.video.VideoImpression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoCollectionPreview implements Serializable {

    private VideoCollectionMetadata metadata = new VideoCollectionMetadata();

    private List<VideoImpression> videos = new ArrayList<>();

    private int videoCount;

    private int popCount;

    public VideoCollectionPreview() {}

    public VideoCollectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(VideoCollectionMetadata metadata) {
        this.metadata = metadata;
    }

    public int getPopCount() {
        return popCount;
    }

    public void setPopCount(final int popCount) {
        this.popCount = popCount;
    }

    public List<VideoImpression> getVideos() {
        return videos;
    }

    public void setVideos(final List<VideoImpression> videos) {
        this.videos = videos;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(final int videoCount) {
        this.videoCount = videoCount;
    }

}