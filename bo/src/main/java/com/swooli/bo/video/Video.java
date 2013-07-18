package com.swooli.bo.video;

import java.io.Serializable;

public class Video implements Serializable {

    private VideoRoot root;

    private VideoMetadata metadata;

    private VideoRating rating;

    public Video() {}

    public VideoMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(final VideoMetadata metadata) {
        this.metadata = metadata;
    }

    public VideoRating getRating() {
        return rating;
    }

    public void setRating(final VideoRating videoRating) {
        this.rating = videoRating;
    }

    public VideoRoot getRoot() {
        return root;
    }

    public void setRoot(final VideoRoot videoRoot) {
        this.root = videoRoot;
    }

}