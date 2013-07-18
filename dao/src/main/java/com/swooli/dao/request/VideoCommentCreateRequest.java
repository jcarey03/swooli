package com.swooli.dao.request;

import com.swooli.bo.video.VideoComment;
import java.io.Serializable;

public class VideoCommentCreateRequest implements Serializable {

    private String videoCollectionId;

    private long videoRootVersion;

    private VideoComment videoComment;

    public VideoComment getVideoComment() {
        return videoComment;
    }

    public void setVideoComment(final VideoComment videoComment) {
        this.videoComment = videoComment;
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