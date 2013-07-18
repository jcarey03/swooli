package com.swooli.bo.video;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="VideoRating")
public class VideoRating implements Serializable {

    private String id;

    private String videoCollectionId;

    private String videoId;

    private long version;

    private int upVoteCount;

    private int downVoteCount;

    private double rating;

    private long creationDate;

    public VideoRating() {}

    public VideoRating(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(final int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(final int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

}