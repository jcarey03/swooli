package com.swooli.dao.request;

import com.swooli.bo.video.VideoVote;

public class VideoVoteRequest {

    private long userProfileVersion;

    private long videoRatingVersion;

    private Boolean previousUpVote;

    private String videoCollectionId;

    private VideoVote vote;

    public long getUserProfileVersion() {
        return userProfileVersion;
    }

    public void setUserProfileVersion(final long userProfileVersion) {
        this.userProfileVersion = userProfileVersion;
    }

    public long getVideoRatingVersion() {
        return videoRatingVersion;
    }

    public void setVideoRatingVersion(final long videoRatingVersion) {
        this.videoRatingVersion = videoRatingVersion;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public Boolean getPreviousUpVote() {
        return previousUpVote;
    }

    public void setPreviousUpVote(final Boolean previousUpVote) {
        this.previousUpVote = previousUpVote;
    }

    public VideoVote getVote() {
        return vote;
    }

    public void setVote(final VideoVote vote) {
        this.vote = vote;
    }

}