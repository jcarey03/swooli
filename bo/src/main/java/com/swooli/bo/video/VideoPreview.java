package com.swooli.bo.video;

import java.io.Serializable;
import org.springframework.data.annotation.Transient;

public class VideoPreview implements Serializable {

    private VideoMetadata metadata;

    @Transient
    private VideoRating rating;

    @Transient
    private int commentCount;

    @Transient
    private int swinkCount;

    @Transient
    private int popCount;

    public VideoRating getRating() {
        return rating;
    }

    public void setRating(final VideoRating videoRating) {
        this.rating = videoRating;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(final int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSwinkCount() {
        return swinkCount;
    }

    public void setSwinkCount(final int swinkCount) {
        this.swinkCount = swinkCount;
    }

    public int getPopCount() {
        return popCount;
    }

    public void setPopCount(final int popCount) {
        this.popCount = popCount;
    }

    public VideoMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(final VideoMetadata videoMetadata) {
        this.metadata = videoMetadata;
    }

}