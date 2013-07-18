package com.swooli.extraction.html.media;

import com.swooli.bo.video.VideoReference;

public class VideoMetadata {

    private VideoReference videoReference;

    private String title;

    private boolean openGraph;

    public VideoReference getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(final VideoReference videoReference) {
        this.videoReference = videoReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean isOpenGraph() {
        return openGraph;
    }

    public void setOpenGraph(final boolean openGraph) {
        this.openGraph = openGraph;
    }

}