package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="VideoVote")
public class VideoVote implements Serializable {

    private String id;

    private String videoId;

    private User createdBy;

    private boolean upVote;

    private long creationDate;

    public VideoVote() {}

    public VideoVote(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isUpVote() {
        return upVote;
    }

    public void setUpVote(final boolean upVote) {
        this.upVote = upVote;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

}